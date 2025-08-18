package com.api_gateway.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
  @Value("${URL_CROSS_ORIGIN}")
  private String allowedOrigin;
  @Value("${URL_CROSS_ORIGIN_VERCEL}")
  private String allowedOriginVercel;
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(allowedOrigin, allowedOriginVercel)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
  }
}
