package com.microservice_news.exceptions;

import com.microservice_news.dto.NewsDTO;
import com.microservice_news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsValidator {

  @Autowired
  private NewsRepository newsRepository;

  public void findById(Long id) throws Exception {
    if (!newsRepository.findById(id).isPresent()) {
      throw new NewsNotExistsException("No hay noticia con el id " + id + " para mostrar");
    }
  }

  public void saveNews(NewsDTO newsDTO) throws Exception {
    //!newsDTO.getTitle().matches("^(?!\\s*$)[a-zA-Z\\s]+$")
    if (newsDTO.getTitle().trim().isEmpty() || newsDTO.getDescription().trim().isEmpty()) {
      throw new NullPointerException("El titulo o descripcion no pueden estar vacios");
    }
  }

  public void updateNews(Long idNews, NewsDTO newsDTO) throws Exception {
    findById(idNews);
    saveNews(newsDTO);
  }

  public void deleteNews(Long idNews) throws Exception {
    findById(idNews);
  }


}
