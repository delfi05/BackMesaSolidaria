package com.microservice_voluntary;

import com.microservice_voluntary.dto.VoluntaryDTO;
import com.microservice_voluntary.exceptions.VoluntaryNotExistsException;
import com.microservice_voluntary.service.VoluntaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = MicroserviceVoluntaryApplication.class)
public class VoluntaryTest {
  @Autowired
  private VoluntaryService voluntaryService;

  @Test
  public void findAll() {
    Exception exception = assertThrows(VoluntaryNotExistsException.class, () -> {
      voluntaryService.findAll();
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void findById() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(VoluntaryNotExistsException.class, () -> {
      voluntaryService.findById(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void saveVoluntary() {
    VoluntaryDTO voluntaryDTO = new VoluntaryDTO("  ", "apellido", "delfy@gemail.com", "2428 4577874");
    Exception exception = assertThrows(NullPointerException.class, () -> {
      voluntaryService.saveVoluntary(voluntaryDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateVoluntaryIdNotExists() {
    Long idNotExist = -8L;
    VoluntaryDTO voluntaryDTO = new VoluntaryDTO("mi nombre", "apellido", "delfy@gemail.com", "2428 4577874");
    Exception exception = assertThrows(Exception.class, () -> {
      voluntaryService.updateVoluntary(idNotExist, voluntaryDTO);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());
  }

  @Test
  public void updateVoluntaryNotExists() {
    Long idExist = 2L;
    VoluntaryDTO voluntaryNow = new VoluntaryDTO("mi nombre", "apellido", "delfy@gemail.com", "24");
    Exception exceptionNew = assertThrows(Exception.class, () -> {
      voluntaryService.updateVoluntary(idExist, voluntaryNow);
    });
    System.out.println("La excepcion del test: " + exceptionNew.getMessage());
  }

  @Test
  public void deleteVoluntary() {
    Long idNotExist = -8L;
    Exception exception = assertThrows(VoluntaryNotExistsException.class, () -> {
      voluntaryService.deleteVoluntary(idNotExist);
    });
    System.out.println("La excepcion del test: " + exception.getMessage());

  }
}
