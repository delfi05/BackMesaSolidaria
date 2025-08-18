package com.microservice_project.service;

import com.microservice_project.dto.ProjectDTO;
import com.microservice_project.entity.Project;
import com.microservice_project.exceptions.ProjectValidator;
import com.microservice_project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProjectService {

  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private ProjectValidator projectValidator;
  @Autowired
  private ConverterService converterService;

  @Value("${upload.directory}") // Define una propiedad en application.properties
  private String uploadDirectory;

  @Transactional(readOnly = true)
  public List<ProjectDTO> findAll() throws Exception {
    List<Project> project = projectRepository.findAll();
    List<ProjectDTO> listProjectsDTO = new ArrayList<>();
    for (Project p : project) {
      ProjectDTO projectDTO = mapToProjectDTO(p);
      String imageName = p.getImage();
      if (imageName != null && !imageName.isEmpty()) {
        //busca el nombre de la imagen en el directorio
        imageFile(projectDTO, imageName);
        projectDTO.setId_project(p.getId_project());
        listProjectsDTO.add(projectDTO);
      }
    }
    return listProjectsDTO;
  }

  @Transactional(readOnly = true)
  public List<ProjectDTO> getAvailable() throws Exception {
    List<Project> project = projectRepository.getAvailableProjects();
    List<ProjectDTO> listProjectsDTO = new ArrayList<>();
    for (Project p : project) {
      ProjectDTO projectDTO = mapToProjectDTO(p);
      String imageName = p.getImage();
      if (imageName != null && !imageName.isEmpty()) {
        //busca el nombre de la imagen en el directorio
        imageFile(projectDTO, imageName);
        projectDTO.setId_project(p.getId_project());
        listProjectsDTO.add(projectDTO);
      }
    }
    return listProjectsDTO;
  }

  public ProjectDTO findById(Long id) throws Exception {
    projectValidator.findById(id);
    Project project = projectRepository.findById(id).get();
    ProjectDTO projectDTO = mapToProjectDTO(project);
    String imageName = project.getImage();
    projectDTO.setId_project(project.getId_project());
    if (imageName != null && !imageName.isEmpty()) {
      //busca el nombre de la imagen en el directorio
      imageFile(projectDTO, imageName);
    }
    return projectDTO;
  }

  public ProjectDTO findAvailableById(Long id) throws Exception {
    projectValidator.findById(id);
    Project project = projectRepository.findAvailableById(id);
    ProjectDTO projectDTO = mapToProjectDTO(project);
    String imageName = project.getImage();
    projectDTO.setId_project(project.getId_project());
    if (imageName != null && !imageName.isEmpty()) {
      //busca el nombre de la imagen en el directorio
      imageFile(projectDTO, imageName);
    }
    return projectDTO;
  }

  public ProjectDTO saveProject(ProjectDTO projectDTO) throws Exception {
    projectValidator.saveProject(projectDTO);
    String nameImageProject = saveImageToDirectory(projectDTO);
    Project project = mapToProject(projectDTO);
    project.setImage(nameImageProject);
    try {
      projectRepository.save(project);
      return new ProjectDTO(project.getTitle(), project.getDescription(), project.getImage(), project.getAvailable());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO updateProject(Long id_project, ProjectDTO projectDTO) throws Exception {
    projectValidator.updateProject(id_project, projectDTO);
    Project project = projectRepository.findById(id_project).orElse(null);

    String oldImageName = project.getImage();
    String newImageName = null;

    // Verifica si se proporcionó una nueva imagen en el DTO
    if (projectDTO.getImage() != null && !projectDTO.getImage().isEmpty()) {
      // Guarda la nueva imagen y obtiene su nombre
      newImageName = saveImageToDirectory(projectDTO);
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
    project = mapToProject(projectDTO);
    project.setId_project(id_project);
    project.setImage(newImageName);
    projectRepository.save(project);
    return new ProjectDTO(project.getTitle(), project.getDescription(), project.getImage(), project.getAvailable());
  }

  public ProjectDTO deleteProject(Long id_project) throws Exception {
    projectValidator.deleteProject(id_project);
    try {
      if (projectRepository.existsById(id_project)) {
        Project project = projectRepository.findById(id_project).get();
        String imageName = project.getImage();
        projectRepository.deleteById(id_project);
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
        return new ProjectDTO(project.getTitle(), project.getDescription(), project.getImage(), project.getAvailable());
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return null;
  }

  private void imageFile(ProjectDTO projectDTO, String imageName) throws Exception {
    File imageFile = new File(uploadDirectory + "/" + imageName);
    if (imageFile.exists()) {
      try {
        //convierte la img en bytes
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String imageDataBase64 = converterService.encode(imageBytes);
        //guarda la img en base 64 para mandarla al front
        projectDTO.setImage(imageDataBase64);
      } catch (IOException e) {
        throw new Exception("Error al leer la imagen: " + e.getMessage());
      }
    }
  }

  private String saveImageToDirectory(ProjectDTO projectDTO) throws Exception {
    byte[] decodedImage = converterService.decode(projectDTO.getImage());
    if (decodedImage == null) {
      throw new Exception("Error al decodificar la imagen Base64.");
    }
    String fileName = projectDTO.getNameImage();
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

  private ProjectDTO mapToProjectDTO(Project project) {
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setTitle(project.getTitle());
    projectDTO.setDescription(project.getDescription());
    projectDTO.setImage(project.getImage());
    projectDTO.setTypeImage(project.getImage().substring(project.getImage().lastIndexOf(".") + 1));
    projectDTO.setAvailable(project.getAvailable());
    return projectDTO;
  }

  private Project mapToProject(ProjectDTO projectDTO) throws Exception {
    Project project = new Project();
    project.setTitle(projectDTO.getTitle());
    project.setDescription(projectDTO.getDescription());
    project.setImage(projectDTO.getNameImage() + "." + projectDTO.getTypeImage());
    project.setAvailable(projectDTO.getAvailable());
    return project;
  }
}
