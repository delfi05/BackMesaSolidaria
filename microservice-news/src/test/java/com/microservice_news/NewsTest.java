package com.microservice_news;

import com.microservice_news.dto.NewsDTO;
import com.microservice_news.exceptions.NewsNotExistsException;
import com.microservice_news.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = MicroserviceNewsApplication.class)
public class NewsTest {

  @Autowired
  private NewsService newsService;

  @Test
  public void findAll() {
    Exception exception = assertThrows(NewsNotExistsException.class, () -> {
      newsService.findAll();
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void findById() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(NewsNotExistsException.class, () -> {
      newsService.findById(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void saveNews() {
    NewsDTO newsDTO = new NewsDTO("  ", "hola soy descripcion", "hola soy epigrafo", "hola soy resumen", "sasasas", new Date(), "descrip img");
    Exception exception = assertThrows(NullPointerException.class, () -> {
      newsService.saveNews(newsDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateNewsIdNotExists() {
    Long idNotExist = -8L;
    NewsDTO newsDTO = new NewsDTO("aefae", "hola soy descripcion", "hola soy epigrafo", "hola soy resumen", "sasasas", new Date(), "descrip img");
    Exception exception = assertThrows(Exception.class, () -> {
      newsService.updateNews(idNotExist, newsDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateNewsNotExists() {
    Long idExist = 2L;
    NewsDTO newNow = new NewsDTO("    ", "hola soy descripcion", "hola soy epigrafo", "hola soy resumen", "sasasas", new Date(), "descrip img");
    Exception exceptionNew = assertThrows(Exception.class, () -> {
      newsService.updateNews(idExist, newNow);
    });
    System.out.println("La excepcion del test: " + exceptionNew.getMessage());
  }

  @Test
  public void deleteNews() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(NewsNotExistsException.class, () -> {
      newsService.deleteNews(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }
}
