package com.microservice_manager.controller;

import com.microservice_manager.dto.ManagerDTO;
import com.microservice_manager.feignClients.model.NewsDTO;
import com.microservice_manager.feignClients.model.ProjectDTO;
import com.microservice_manager.feignClients.model.VoluntaryDTO;
import com.microservice_manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {
  @Autowired
  private ManagerService managerService;

  @GetMapping("")
  public ResponseEntity<?> getAll() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findAll());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos las managers.\"}");
    }
  }

  @GetMapping("/{id_manager}")
  public ResponseEntity<?> getManagerByID(@PathVariable Long id_manager) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findById(id_manager));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el manager por ID.\"}");
    }
  }

  @GetMapping("/findByEmail/{email}")
  public ResponseEntity<?> getManagerByEmail(@PathVariable String email) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findByEmail(email));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el manager por Email.\"}");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> saveManager(@Valid @RequestBody ManagerDTO managerDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.saveManager(managerDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginManager(@Valid @RequestBody ManagerDTO managerDTO) throws Exception {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.loginManager(managerDTO)); // Devolver el token en el cuerpo
    } catch (AuthenticationException e) {
      return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/validateToken")
  public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.validateToken(authorizationHeader));
    } catch (AuthenticationException e) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/updateManager/{id_manager}")
  public ResponseEntity<?> updateManager(@PathVariable Long id_manager, @Valid @RequestBody ManagerDTO managerDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.updateManager(id_manager, managerDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar el manager.\"}");
    }
  }

  @DeleteMapping("/deleteManager/{id_manager}")
  public ResponseEntity<?> deleteManager(@PathVariable Long id_manager) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.deleteManager(id_manager));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar el manager, intente nuevamente.\"}");
    }
  }

  @GetMapping("/logoutManager")
  public ResponseEntity<String> logoutManager(@RequestHeader("Authorization") String token) throws Exception {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.logoutManager(token)); // Devolver el token en el cuerpo
    } catch (AuthenticationException e) {
      return new ResponseEntity<>("La sesion no se cerro correctamente, intente nuevamente", HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/isTokenBlacklisted")
  public ResponseEntity<Boolean> isTokenBlacklisted(@RequestHeader("Authorization") String token) {
    String cleanToken = token.replace("Bearer ", "");
    boolean isBlacklisted = managerService.isTokenBlacklisted(cleanToken);
    return ResponseEntity.ok(isBlacklisted);
  }

  @PostMapping("/forgotPassword")
  public ResponseEntity<String> forgotPassword(@Valid @RequestBody ManagerDTO managerDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.forgotPassword(managerDTO.getEmail()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo actualizar la nueva contraseña, por favor verifique el email e intente nuevamente.\"}");
    }
  }

  @GetMapping("/getAllNews")
  public ResponseEntity<?> getAllNews() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.getAllNews());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo obtener todas las noticias, por favor intente nuevamente.\"}");
    }
  }

  @GetMapping("/getNewsById/{id_news}")
  public ResponseEntity<?> getNewsByID(@PathVariable Long id_news) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findNewsById(id_news));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el noticias por ID.\"}");
    }
  }

  @PostMapping("/saveNews")
  public ResponseEntity<?> saveNews(@Valid @RequestBody NewsDTO newsDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.saveNews(newsDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateNews/{id_news}")
  public ResponseEntity<?> updateNews(@PathVariable Long id_news, @Valid @RequestBody NewsDTO newsDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.updateNews(id_news, newsDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar la noticia.\"}");
    }
  }

  @DeleteMapping("/deleteNews/{id_news}")
  public ResponseEntity<?> deleteNews(@PathVariable Long id_news) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.deleteNews(id_news));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar la noticia, intente nuevamente.\"}");
    }
  }

  @GetMapping("/getAllProjects")
  public ResponseEntity<?> getAllProjects() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findAllProjects());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos los proyectos.\"}");
    }
  }

  @GetMapping("/getProjectsById/{id_project}")
  public ResponseEntity<?> getProjectByID(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findProjectsById(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el proyecto por ID.\"}");
    }
  }

  @GetMapping("/getAvailableById/{id_project}")
  public ResponseEntity<?> getAvailableProjectsById(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findAvailableProjectsById(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el proyecto disponible por ID.\"}");
    }
  }

  @PostMapping("/saveProject")
  public ResponseEntity<?> saveProject(@Valid @RequestBody ProjectDTO projectDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.saveProject(projectDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateProject/{id_project}")
  public ResponseEntity<?> updateProject(@PathVariable Long id_project, @Valid @RequestBody ProjectDTO projectDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.updateProject(id_project, projectDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar el proyecto.\"}");
    }
  }

  @DeleteMapping("/deleteProject/{id_project}")
  public ResponseEntity<?> deleteProject(@PathVariable Long id_project) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.deleteProject(id_project));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar el proyecto, intente nuevamente.\"}");
    }
  }

  @GetMapping("/getVoluntaries")
  public ResponseEntity<?> getAllVoluntaries() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findAllVoluntaries());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos los voluntarios.\"}");
    }
  }

  @GetMapping("/getVoluntaryById/{id_voluntary}")
  public ResponseEntity<?> getVoluntaryByID(@PathVariable Long id_voluntary) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findVoluntaryById(id_voluntary));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el voluntario por ID.\"}");
    }
  }

  @GetMapping("/getVoluntaryByEmail/{email}")
  public ResponseEntity<?> getVoluntaryByEmail(@PathVariable String email) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.findVoluntaryByEmail(email));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el voluntario por Email.\"}");
    }
  }

  @PostMapping("/saveVoluntary")
  public ResponseEntity<?> saveVoluntary(@RequestBody VoluntaryDTO voluntaryDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.saveVoluntary(voluntaryDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateVoluntary/{id_voluntary}")
  public ResponseEntity<?> updateVoluntary(@PathVariable Long id_voluntary, @RequestBody VoluntaryDTO voluntaryDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.updateVoluntary(id_voluntary, voluntaryDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar el voluntario.\"}");
    }
  }

  @DeleteMapping("/deleteVoluntary/{id_voluntary}")
  public ResponseEntity<?> deleteVoluntary(@PathVariable Long id_voluntary) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(managerService.deleteVoluntary(id_voluntary));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar el voluntario, intente nuevamente.\"}");
    }
  }
}
