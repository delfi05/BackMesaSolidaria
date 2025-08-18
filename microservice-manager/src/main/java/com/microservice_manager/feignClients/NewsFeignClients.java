package com.microservice_manager.feignClients;

import com.microservice_manager.feignClients.model.NewsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "microservice-news")
public interface NewsFeignClients {

  @GetMapping("/api/news")
  List<NewsDTO> findAll();

  @GetMapping("/api/news/{id_news}")
  NewsDTO findById(@PathVariable Long id_news);

  @PostMapping("/api/news")
  NewsDTO saveNews(@RequestBody NewsDTO newsDTO);

  @PutMapping("/api/news/updateNews/{id_news}")
  NewsDTO updateNews(@PathVariable Long id_news, @RequestBody NewsDTO newsDTO);

  @DeleteMapping("/api/news/deleteNews/{id_news}")
  NewsDTO deleteNews(@PathVariable Long id_news);
}
