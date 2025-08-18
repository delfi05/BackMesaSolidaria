package com.microservice_project.exceptions;

import com.microservice_project.dto.ProjectDTO;
import com.microservice_project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {
  @Autowired
  private ProjectRepository projectRepository;

  public void findById(Long id) throws Exception {
    projectRepository.findById(id).orElseThrow(() -> new ProjectNotExistsException("No existe el proyecto"));
  }

  public void saveProject(ProjectDTO projectDTO) throws Exception {
    //!nprojectDTO.getTitle().matches("^(?!\\s*$)[a-zA-Z\\s]+$")
    if (projectDTO.getTitle().trim().isEmpty() || projectDTO.getDescription().trim().isEmpty()) {
      throw new NullPointerException("El titulo o descripcion no pueden estar vacios");
    }
  }

  public void updateProject(Long idProject, ProjectDTO ProjectDTO) throws Exception {
    findById(idProject);
    saveProject(ProjectDTO);
  }

  public void deleteProject(Long idProject) throws Exception {
    findById(idProject);
  }
}
