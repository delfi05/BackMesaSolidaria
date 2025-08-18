package com.api_gateway.Config;

import com.api_gateway.Security.AuthorityConstant;
import com.api_gateway.Security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain authFilterChain(final HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable);
    http
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http
        .securityMatcher("/auth/**")
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/forgotPassword").permitAll()
            // y con este le doy la autoridad solo al manager de acceder
            .requestMatchers(HttpMethod.POST, "/auth/register").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.GET, "/auth/logout").hasAuthority(AuthorityConstant._MANAGER)
        )
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
  @Bean
  public SecurityFilterChain apiFilterChain(final HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable);
    http
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, "/api/voluntaries").permitAll()
            // POST
            .requestMatchers(HttpMethod.POST, "/api/managers/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.POST, "/api/news/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.POST, "/api/projects/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.POST, "/api/voluntaries/**").hasAuthority(AuthorityConstant._MANAGER)
            // PUT
            .requestMatchers(HttpMethod.PUT, "/api/managers/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.PUT, "/api/news/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.PUT, "/api/voluntaries/**").hasAuthority(AuthorityConstant._MANAGER)
            // DELETE
            .requestMatchers(HttpMethod.DELETE, "/api/managers/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.DELETE, "/api/news/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.DELETE, "/api/voluntaries/**").hasAuthority(AuthorityConstant._MANAGER)
            // GET MANAGER
            .requestMatchers(HttpMethod.GET, "/api/managers/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.GET, "/api/news/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/projects/getAvailable").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/projects/getAvailableById/{id_project}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAuthority(AuthorityConstant._MANAGER)
            .requestMatchers(HttpMethod.GET, "/api/voluntaries/**").hasAuthority(AuthorityConstant._MANAGER)
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}