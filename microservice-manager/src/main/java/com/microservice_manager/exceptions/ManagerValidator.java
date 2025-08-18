package com.microservice_manager.exceptions;

import com.microservice_manager.dto.ManagerDTO;
import com.microservice_manager.entity.Manager;
import com.microservice_manager.repository.ManagerRepository;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManagerValidator {
  private final int PASSWORD = 8;
  @Autowired
  private ManagerRepository managerRepository;

  public void findById(Long id) throws ManagerNotExistsException {
    if (!managerRepository.findById(id).isPresent())
      throw new ManagerNotExistsException("No existe un manager con id: " + id);
  }

  public void findByEmail(String email) throws ManagerNotExistsException {
    if (!managerRepository.findByEmail(email).isPresent())
      throw new ManagerNotExistsException("No existe un manager con email: " + email);
  }

  public void saveManager(ManagerDTO managerDTO) throws Exception {
    isRepeatEmail(managerDTO.getEmail());
    validateEmail(managerDTO.getEmail());
    validatePassword(managerDTO.getPassword());
    if (!managerDTO.getName().matches("^(?!\\s*$)[a-zA-Z\\s]+$") ||
        !managerDTO.getLastName().matches("^(?!\\s*$)[a-zA-Z\\s]+$"))
      throw new ManagerNameLastNameException("El nombre o apellido no pueden ser vacíos o contener solo espacios");
  }

  public void updateManager(Long idManager, ManagerDTO managerDTO) throws Exception {
    findById(idManager);
    Manager managerDDBB = managerRepository.findById(idManager).get();
    if(!managerDDBB.getEmail().equals(managerDTO.getEmail()))
      isRepeatEmail(managerDTO.getEmail());
    if (managerDTO.getPassword() != null)
      validatePassword(managerDTO.getPassword());
    if (!managerDTO.getName().matches("^(?!\\s*$)[a-zA-Z\\s]+$") ||
        !managerDTO.getLastName().matches("^(?!\\s*$)[a-zA-Z\\s]+$"))
      throw new ManagerNameLastNameException("El nombre o apellido no pueden ser vacíos o contener solo espacios");
  }

  public void deleteManager(Long idManager) throws ManagerNotExistsException {
    findById(idManager);
  }

  public void validatePassword(String password) throws ManagerPasswordException {
    if (password.length() < PASSWORD || password.contains(" ")) {
      throw new ManagerPasswordException("La contraseña es menor a 8 caracteres o no puede estar vacia");
    }
  }

  public void validateEmail(String email) throws ManagerNotExistsException {
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      throw new ManagerNotExistsException("El correo no tiene un formato válido: " + email);
    }
  }

  public void isRepeatEmail(String email) throws ManagerAlreadyExistsException {
    if (managerRepository.findByEmail(email).isPresent())
      throw new ManagerAlreadyExistsException("Ya existe un manager con ese email");
  }
}
