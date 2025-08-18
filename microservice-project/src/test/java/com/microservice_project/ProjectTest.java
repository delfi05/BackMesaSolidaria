package com.microservice_project;

import com.microservice_project.dto.ProjectDTO;
import com.microservice_project.exceptions.ProjectNotExistsException;
import com.microservice_project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = MicroserviceProjectApplication.class)
public class ProjectTest {
  @Autowired
  private ProjectService projectService;

  @Test
  public void findAll() {
    Exception exception = assertThrows(ProjectNotExistsException.class, () -> {
      projectService.findAll();
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void findById() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(ProjectNotExistsException.class, () -> {
      projectService.findById(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void saveProject() {
    ProjectDTO projectDTO = new ProjectDTO("  ", "hola soy descripcion", "img", true);
    Exception exception = assertThrows(NullPointerException.class, () -> {
      projectService.saveProject(projectDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateProjectIdNotExists() {
    Long idNotExist = -8L;
    ProjectDTO projectDTO = new ProjectDTO("aefae", "hola soy descripcion", "descrip img", true);
    Exception exception = assertThrows(Exception.class, () -> {
      projectService.updateProject(idNotExist, projectDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateProjectNotExists() {
    Long idExist = 2L;
    ProjectDTO projectNow = new ProjectDTO("    ", "hola soy descripcion", "descrip img", true);
    Exception exceptionNew = assertThrows(Exception.class, () -> {
      projectService.updateProject(idExist, projectNow);
    });
    System.out.println("La excepcion del test: " + exceptionNew.getMessage());
  }

  @Test
  public void deleteProject() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(ProjectNotExistsException.class, () -> {
      projectService.deleteProject(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }
}
