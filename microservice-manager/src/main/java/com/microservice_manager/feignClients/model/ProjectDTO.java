package com.microservice_manager.feignClients.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
  private Long id_project;
  private String title;
  private String description;
  private String image;
  private String nameImage;
  private String typeImage;
  private Boolean available;
}