package com.microservice_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

  public ProjectDTO(String title, String description, String image, Boolean available) {
    this.title = title;
    this.description = description;
    this.image = image;
    this.available = available;
  }
  public ProjectDTO(String title, String description, String image, String nameImage, String typeImage, Boolean available) {
    this.title = title;
    this.description = description;
    this.image = image;
    this.nameImage = nameImage;
    this.typeImage = typeImage;
    this.available = available;
  }
}
