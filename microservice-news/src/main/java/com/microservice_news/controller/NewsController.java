package com.microservice_news.controller;

import com.microservice_news.dto.NewsDTO;
import com.microservice_news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

  @Autowired
  private NewsService newsService;

  @GetMapping("")
  public ResponseEntity<?> getAll() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(newsService.findAll());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todas las noticias.\"}");
    }
  }

  @GetMapping("/{id_news}")
  public ResponseEntity<?> getNewsByID(@PathVariable Long id_news) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(newsService.findById(id_news));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el noticias por ID.\"}");
    }
  }

  @PostMapping("")
  public ResponseEntity<?> saveNews(@RequestBody NewsDTO newsDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(newsService.saveNews(newsDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateNews/{id_news}")
  public ResponseEntity<?> updateNews(@PathVariable Long id_news, @RequestBody NewsDTO newsDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(newsService.updateNews(id_news, newsDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar la noticia.\"}");
    }
  }

  @DeleteMapping("/deleteNews/{id_news}")
  public ResponseEntity<?> deleteNews(@PathVariable Long id_news) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(newsService.deleteNews(id_news));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar la noticia, intente nuevamente.\"}");
    }
  }
}
