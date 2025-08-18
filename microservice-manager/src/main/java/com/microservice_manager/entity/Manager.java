package com.microservice_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id_manager;
  private String name;
  private String lastName;
  @Column(unique = true, nullable = false)
  private String email;
  private String password;
  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "authority_name", referencedColumnName = "name")
  private Authority authority;
}
