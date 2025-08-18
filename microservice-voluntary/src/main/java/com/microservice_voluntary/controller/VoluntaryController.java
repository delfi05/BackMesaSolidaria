package com.microservice_voluntary.controller;

import com.microservice_voluntary.dto.VoluntaryDTO;
import com.microservice_voluntary.service.VoluntaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voluntaries")
@RequiredArgsConstructor
public class VoluntaryController {
  @Autowired
  private VoluntaryService voluntaryService;

  @GetMapping("")
  public ResponseEntity<?> getAll() {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.findAll());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo todos los voluntarios.\"}");
    }
  }

  @GetMapping("/{id_voluntary}")
  public ResponseEntity<?> getVoluntaryByID(@PathVariable Long id_voluntary) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.findById(id_voluntary));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el voluntario por ID.\"}");
    }
  }

  @GetMapping("/findByEmail/{email}")
  public ResponseEntity<?> getVoluntaryByEmail(@PathVariable String email) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.findByEmail(email));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde obteniendo el voluntario por Email.\"}");
    }
  }

  @PostMapping("")
  public ResponseEntity<?> saveVoluntary(@RequestBody VoluntaryDTO voluntaryDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.saveVoluntary(voluntaryDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
    }
  }

  @PutMapping("/updateVoluntary/{id_voluntary}")
  public ResponseEntity<?> updateVoluntary(@PathVariable Long id_voluntary, @RequestBody VoluntaryDTO voluntaryDTO) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.updateVoluntary(id_voluntary, voluntaryDTO));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde para editar el voluntario.\"}");
    }
  }

  @DeleteMapping("/deleteVoluntary/{id_voluntary}")
  public ResponseEntity<?> deleteVoluntary(@PathVariable Long id_voluntary) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(voluntaryService.deleteVoluntary(id_voluntary));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo borrar el voluntario, intente nuevamente.\"}");
    }
  }
}
