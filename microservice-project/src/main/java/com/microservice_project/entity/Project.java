package com.microservice_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id_project;
  private String title;
  @Column(nullable = false, length = 10000)
  private String description;
  private String image;
  @Column(nullable = false)
  private Boolean available;
}
