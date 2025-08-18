package com.microservice_manager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistToken {
  @Id
  private String token;
  private LocalDateTime createdAt;

  public BlacklistToken(String token) {
    this.token = token;
    this.createdAt = LocalDateTime.now(); // Establecer la fecha de creación al guardar el token
  }
}
