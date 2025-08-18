package com.microservice_news.dto;

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
  private Date date;
  private String description;
  private String epigraph;
  private String summary;
  private String image;
  private String nameImage;
  private String typeImage;
  private String imageCaption;

  public NewsDTO(String title, Date date) {
    this.title = title;
    this.date = date;
  }

  public NewsDTO(String title, String description, String epigraph, String summary, String image, Date date, String imageCaption) {
    this.title = title;
    this.description = description;
    this.epigraph = epigraph;
    this.summary = summary;
    this.image = image;
    this.date = date;
    this.imageCaption = imageCaption;
  }

  public NewsDTO(String title, String imageCaption, String typeImage, String nameImage, String image, String summary, String description, Date date, String epigraph) {
    this.title = title;
    this.imageCaption = imageCaption;
    this.typeImage = typeImage;
    this.nameImage = nameImage;
    this.image = image;
    this.summary = summary;
    this.description = description;
    this.date = date;
    this.epigraph = epigraph;
  }
}
