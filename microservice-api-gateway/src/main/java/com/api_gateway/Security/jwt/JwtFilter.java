package com.api_gateway.Security.jwt;

import com.api_gateway.FeignClients.ManagerFeignClients;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

  @Value("${jwt.secret}")
  private String SECRET;

  public static final String AUTHORIZATION_HEADER = "Authorization";

  @Autowired
  private ManagerFeignClients managerFeignClients;
  private static final String AUTHORITIES_KEY = "auth";
  private JwtParser jwtParser;
  private SecretKey key;

  @PostConstruct // Move initialization to a method executed after dependency injection
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.jwtParser = Jwts.parser().verifyWith(key).build();
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String jwt = resolveToken(request);
    try {
      if (StringUtils.hasText(jwt)) {
        ResponseEntity<Boolean> validationResponse = managerFeignClients.validateToken("Bearer " + jwt);
        if (validationResponse.getBody() != null && validationResponse.getBody()) {
          // Verificar la lista negra
          ResponseEntity<Boolean> blacklistResponse = managerFeignClients.isTokenBlacklisted("Bearer " + jwt);
          if (blacklistResponse.getBody() != null && !blacklistResponse.getBody()) {
            Authentication authentication = getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          } else {
            log.info("REST request UNAUTHORIZED - Token en la lista negra.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
          }
        } else {
          log.info("REST request UNAUTHORIZED - Token inválido.");
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return;
        }
      }
    } catch (Exception e) {
      log.info("REST request UNAUTHORIZED - Error al validar el token: {}", e.getMessage());
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public Authentication getAuthentication(String token) {
    Claims claims = jwtParser.parseSignedClaims(token).getPayload();

    Collection<? extends GrantedAuthority> authorities = Arrays
        .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        .filter(auth -> !auth.trim().isEmpty())
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }
}
