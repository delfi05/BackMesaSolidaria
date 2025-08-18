package com.microservice_manager.feignClients;

import com.microservice_manager.feignClients.model.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "microservice-project")
public interface ProjectFeignClients {

  @GetMapping("/api/projects")
  List<ProjectDTO> getAll();

  @GetMapping("/api/projects/getAvailable")
  List<ProjectDTO> getAvailableProjects();

  @GetMapping("/api/projects/{id_project}")
  ProjectDTO getProjectByID(@PathVariable Long id_project);

  @GetMapping("/api/projects/getAvailableById/{id_project}")
  ProjectDTO getAvailableProjectByID(@PathVariable Long id_project);

  @PostMapping("/api/projects")
  ProjectDTO saveProject(@RequestBody ProjectDTO projectDTO);

  @PutMapping("/api/projects/updateProject/{id_project}")
  ProjectDTO updateProject(@PathVariable Long id_project, @RequestBody ProjectDTO projectDTO);

  @DeleteMapping("/api/projects/deleteProject/{id_project}")
  ProjectDTO deleteProject(@PathVariable Long id_project);
}