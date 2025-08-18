package com.microservice_manager.Security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
  private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

  @Value("${jwt.secret}")
  private String SECRET;
  private static final String AUTHORITIES_KEY = "auth";

  private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

  private SecretKey key;

  private JwtParser jwtParser;

  private int tokenValidityInMilliseconds;

  @PostConstruct // Move initialization to a method executed after dependency injection
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.jwtParser = Jwts.parser().verifyWith(key).build();
    this.tokenValidityInMilliseconds = 60000 * 30;
  }

  public String createToken(Authentication authentication, Long id_manager, String name, String lastName) {
    String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenValidityInMilliseconds);

    return Jwts
        .builder()
        .subject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .claim("id_manager", id_manager)
        .claim("name", name)
        .claim("lastName", lastName)
        .signWith(key)
        .expiration(validity)
        .issuedAt(new Date())
        .compact();
  }

  public boolean validateToken(String authToken) {
    try {
      log.info("Token JWT recibido: {}", authToken); // Imprimir el token
      final var claims = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(authToken);
      log.info("Reclamaciones del token: {}", claims.getPayload());
      this.checkTokenExpiration(claims);
      log.info("Validación del token exitosa");
      return true;
    } catch (UnsupportedJwtException e) {
      log.trace(INVALID_JWT_TOKEN, e);
    } catch (MalformedJwtException e) {
      log.trace(INVALID_JWT_TOKEN, e);
    } catch (SignatureException e) {
      log.trace(INVALID_JWT_TOKEN, e);
    } catch (IllegalArgumentException e) {
      log.error("Token validation error {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("Token Expirado");
    }
    log.info("Validación del token fallida"); // Imprimir fallo
    return false;
  }

  private void checkTokenExpiration(Jws<Claims> token) {
    try {
      final var payload = token.getPayload();
      if (payload.getExpiration().before(new Date()) || payload.getIssuedAt().after(new Date((new Date()).getTime() + this.tokenValidityInMilliseconds)))
        throw new ExpiredJwtException(null, null, null);
    } catch (Exception e) {
      throw new ExpiredJwtException(null, null, null);
    }
  }
}
