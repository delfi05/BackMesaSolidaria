package com.microservice_news.service;

import com.microservice_news.dto.NewsDTO;
import com.microservice_news.entity.News;
import com.microservice_news.exceptions.NewsValidator;
import com.microservice_news.repository.NewsRepository;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NewsService {

  @Autowired
  private NewsRepository newsRepository;
  @Autowired
  private NewsValidator newsValidator;
  @Autowired
  private ConverterService converterService;

  @Value("${upload.directory}") // Define una propiedad en application.properties
  private String uploadDirectory;

  @Transactional(readOnly = true)
  public List<NewsDTO> findAll() throws Exception {
      List<News> news = newsRepository.findAll();
      List<NewsDTO> listNewsDTO = new ArrayList<>();
      for (News n : news) {
        NewsDTO newsDTO = mapToNewsDTO(n);
        String imageName = n.getImage();
        if (imageName != null && !imageName.isEmpty()) {
          //busca el nombre de la imagen en el directorio
          imageFile(newsDTO, imageName);
          newsDTO.setId_news(n.getId_news());
          listNewsDTO.add(newsDTO);
        }
      }
      return listNewsDTO;
  }

  public NewsDTO findById(Long id) throws Exception {
    newsValidator.findById(id);
      News news = newsRepository.findById(id).get();
      NewsDTO newsDTO = mapToNewsDTO(news);
      String imageName = news.getImage();
      newsDTO.setId_news(news.getId_news());
       if (imageName != null && !imageName.isEmpty()) {
      //busca el nombre de la imagen en el directorio
      imageFile(newsDTO, imageName);
    }
    return newsDTO;
  }


  public NewsDTO saveNews(NewsDTO newsDTO) throws Exception {
    System.out.println("se agrega noticia" + newsDTO);
    newsValidator.saveNews(newsDTO);
    String nameImageNews = saveImageToDirectory(newsDTO);
    News news = mapToNews(newsDTO);
    news.setImage(nameImageNews);
    try {
      newsRepository.save(news);
      return new NewsDTO(news.getTitle(), news.getDescription(), news.getEpigraph(), news.getSummary(), news.getImage(), news.getDate(), news.getImageCaption());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public NewsDTO updateNews(Long id_news, NewsDTO newsDTO) throws Exception {
    newsValidator.updateNews(id_news, newsDTO);
    News news = newsRepository.findById(id_news).orElse(null);
    String oldImageName = news.getImage();
    String newImageName = null;
    if(newsDTO.getImage() != null && !newsDTO.getImage().isEmpty()) {
      newImageName = saveImageToDirectory(newsDTO);
      File imageFile = new File(uploadDirectory + "/" + oldImageName);

      // Elimina la imagen anterior si existía
      if (oldImageName != null && !oldImageName.isEmpty() && !oldImageName.equals(newImageName)) {
        if (imageFile.delete()) { //elimina img
          System.out.println("Imagen eliminada: " + imageFile.getAbsolutePath());
        } else {
          System.err.println("No se pudo eliminar la imagen: " + imageFile.getAbsolutePath());
        }
      }
    }
    news = mapToNews(newsDTO);
    news.setImage(newImageName);
    news.setId_news(id_news);
    newsRepository.save(news);
    return new NewsDTO(news.getTitle(), news.getDescription(), news.getEpigraph(), news.getSummary(), news.getImage(), news.getDate(), news.getImageCaption());
  }

  public NewsDTO deleteNews(Long id_news) throws Exception {
    newsValidator.deleteNews(id_news);
    try {
      if (newsRepository.existsById(id_news)) {
        News news = newsRepository.findById(id_news).get();
        String imageName = news.getImage();
        newsRepository.deleteById(id_news);
        if (imageName != null && !imageName.isEmpty()) {
          //crea la ruta d ela img x ams q exista o no
          File imageFile = new File(uploadDirectory + "/" + imageName);
          if (imageFile.exists()) {
            if (imageFile.delete()) { //elimina img
              System.out.println("Imagen eliminada: " + imageFile.getAbsolutePath());
            } else {
              System.err.println("No se pudo eliminar la imagen: " + imageFile.getAbsolutePath());
            }
          } else {
            System.out.println("La imagen no existe en el directorio: " + imageFile.getAbsolutePath());
          }
        }
        return new NewsDTO(news.getTitle(), news.getDescription(), news.getEpigraph(), news.getSummary(), news.getImage(), news.getDate(), news.getImageCaption());
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return null;
  }
  private void imageFile(NewsDTO newsDTO, String imageName) throws Exception {
    File imageFile = new File(uploadDirectory + "/" + imageName);
    if (imageFile.exists()) {
      try {
        //convierte la img en bytes
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String imageDataBase64 = converterService.encode(imageBytes);
        //guarda la img en base 64 para mandarla al front
        newsDTO.setImage(imageDataBase64);
      } catch (IOException e) {
        throw new Exception("Error al leer la imagen: " + e.getMessage());
      }
    }
  }

  private String saveImageToDirectory(NewsDTO newsDTO) throws Exception {
    byte[] decodedImage = converterService.decode(newsDTO.getImage());
    if (decodedImage == null) {
      throw new Exception("Error al decodificar la imagen Base64.");
    }
    String fileName = newsDTO.getNameImage();
    String fileExtension = "";
    if (fileName != null && fileName.contains(".")) {
      //extrae lo q esta desp del punto = la extension
      fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    //aca podria ir fileName pero se recomienda que se guarde un nombre unico
    String uniqueFileName = UUID.randomUUID().toString() + (fileExtension.isEmpty() ? "" : "." + fileExtension);
    String filePath = uploadDirectory + "/" + uniqueFileName;

    try {
      File directory = new File(uploadDirectory);
      if (!directory.exists()) {
        directory.mkdirs();
      }
      File newFile = new File(filePath);
      //fileoutputstream toma los datos binarios de la imagen decodificada
      // y los guarda como un archivo real en el sistema de archivos del servidor.
      try (FileOutputStream fos = new FileOutputStream(newFile)) {
        fos.write(decodedImage);
      }
    } catch (IOException e) {
      throw new Exception("Error al guardar la imagen: " + e.getMessage());
    }
    return uniqueFileName;
  }
  private NewsDTO mapToNewsDTO(News news) throws Exception {
    NewsDTO newsDTO = new NewsDTO();
    newsDTO.setTitle(news.getTitle());
    newsDTO.setDescription(news.getDescription());
    newsDTO.setEpigraph(news.getEpigraph());
    newsDTO.setSummary(news.getSummary());
    newsDTO.setImage(news.getImage());
    newsDTO.setTypeImage(news.getImage().substring(news.getImage().lastIndexOf(".") + 1));
    newsDTO.setDate(news.getDate());
    newsDTO.setImageCaption(news.getImageCaption());
    return newsDTO;
  }
  private News mapToNews(NewsDTO newsDTO) throws Exception {
    News news = new News();
    news.setTitle(newsDTO.getTitle());
    news.setDescription(newsDTO.getDescription());
    news.setEpigraph(newsDTO.getEpigraph());
    news.setSummary(newsDTO.getSummary());
    news.setImage(newsDTO.getNameImage() + "." + newsDTO.getTypeImage());
    news.setDate(newsDTO.getDate());
    news.setImageCaption(newsDTO.getImageCaption());
    return news;
  }
}
