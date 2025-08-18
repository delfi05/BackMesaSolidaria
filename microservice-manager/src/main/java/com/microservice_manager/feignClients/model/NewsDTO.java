package com.microservice_manager.feignClients.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
  private Long id_news;
  private String title;
  private String description;
  private String epigraph;
  private String summary;
  private String image;
  private String nameImage;
  private String typeImage;
  private Date date;
  private String imageCaption;
}