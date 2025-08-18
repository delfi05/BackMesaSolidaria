package com.microservice_manager;

import com.microservice_manager.dto.ManagerDTO;
import com.microservice_manager.exceptions.ManagerNotExistsException;
import com.microservice_manager.service.EmailService;
import com.microservice_manager.service.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MicroserviceManagerApplication.class)
public class ManagerTest {
  private ManagerDTO managerDTO;
  @Autowired
  private ManagerService managerService;
  @MockBean
  private EmailService emailService;
  @Test
  public void findAll() {
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.findAll();
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void findById() {
    Long idInexistente = -9L;
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.findById(idInexistente);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void findByEmail() {
    String emailNoExiste = " ";
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.findByEmail(emailNoExiste);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void saveManager() {
    //cuando se modifica que la contraseña tenga mas de 8 caracteres, salta el error de q el apellido no puede ser vacio
    managerDTO = new ManagerDTO(" ", "jdjeee", "p@gmail.com", "999hd8ljj");
    Exception exception = assertThrows(Exception.class, () -> {
      managerService.saveManager(managerDTO);
    });
    String expectedMessage = exception.getMessage();
    boolean containsExpectedMessage = expectedMessage.contains("Ya existe un manager con ese email") ||
        expectedMessage.contains("La contraseña es menor a 8 caracteres") ||
        expectedMessage.contains("El nombre o apellido no pueden ser vacios");

    // muestra excepcion cuando desde la entidad no lo permite o que se revierte desde la base de datos (rollback)
    assertTrue(containsExpectedMessage, "El mensaje de la excepción no es esperado: " + expectedMessage);
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void updateManager() {
    managerDTO = new ManagerDTO("prueba", " ", "putooooooo@g.c", "j9999ko999");
    Long idExistente = 10L;
    Exception exception = assertThrows(Exception.class, () -> {
      managerService.updateManager(idExistente, managerDTO);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());


    ManagerDTO managerDTOId = new ManagerDTO("holahola", " ", "hoholala@g.c", "999");
    Long idInexistente = -80L; //para que falle x id
    Exception exceptionId = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.updateManager(idInexistente, managerDTOId);
    });
    System.out.println("Mensaje del test Id inexistente: " + exceptionId.getMessage());
  }

  @Test
  public void deleteManager() {
    Long idExistente = -8L;
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.deleteManager(idExistente);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void loginManager() {
    ManagerDTO managerDTOLogin = new ManagerDTO("asdasd@asdasd.asdad");
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.loginManager(managerDTOLogin);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void forgotPasswordManager() {
    String email = "asasdad@asdasd.asdad";
    Exception exception = assertThrows(ManagerNotExistsException.class, () -> {
      managerService.forgotPassword(email);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }

  @Test
  public void logoutManager() {
    String token = "jajanoexistetoken";
    Exception exception = assertThrows(Exception.class, () -> {
      managerService.logoutManager(token);
    });
    System.out.println("Mensaje del test: " + exception.getMessage());
  }
}
