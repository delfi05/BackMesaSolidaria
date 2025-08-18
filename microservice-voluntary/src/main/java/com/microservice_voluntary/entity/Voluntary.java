package com.microservice_voluntary.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voluntary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id_voluntary;
  private String name;
  private String lastName;
  @Column(unique = true, nullable = false)
  private String email;
  private String phone;

  public Voluntary(String name, String lastName, String email, String phone) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }
}
