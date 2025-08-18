package com.microservice_project.controller;

import com.microservice_project.dto.ProjectDTO;
import com.microservice_project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
  @Autowired
  private ProjectService projectService;

  @GetMapping("")
  public ResponseEntity<?> getAll() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.findAll());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos los proyectos.\"}");
    }
  }

  @GetMapping("/getAvailable")
  public ResponseEntity<?> getAvailable() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.getAvailable());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos los proyectos disponibles.\"}");
    }
  }

  @GetMapping("/{id_project}")
  public ResponseEntity<?> getProjectByID(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.findById(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el proyecto por ID.\"}");
    }
  }

  @GetMapping("/getAvailableById/{id_project}")
  public ResponseEntity<?> getAvailableProjectByID(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.findAvailableById(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el proyecto disponible por ID.\"}");
    }
  }

  @PostMapping("")
  public ResponseEntity<?> saveProject(@RequestBody ProjectDTO projectDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.saveProject(projectDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateProject/{id_project}")
  public ResponseEntity<?> updateProject(@PathVariable Long id_project, @RequestBody ProjectDTO projectDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(id_project, projectDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar el proyecto.\"}");
    }
  }

  @DeleteMapping("/deleteProject/{id_project}")
  public ResponseEntity<?> deleteProject(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(projectService.deleteProject(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar el proyecto, intente nuevamente.\"}");
    }
  }
}
