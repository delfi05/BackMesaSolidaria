package com.microservice_news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id_news;
  @Column(nullable = false)
  private String title;
  private Date date;
  @Column(nullable = false, length = 1000)
  private String epigraph;
  @Column(nullable = false, length = 1000)
  private String summary;
  @Column(nullable = false, length = 10000)
  private String description;
  private String image;
  private String imageCaption;

}
