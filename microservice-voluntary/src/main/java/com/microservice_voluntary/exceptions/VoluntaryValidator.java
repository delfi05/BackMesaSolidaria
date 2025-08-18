package com.microservice_voluntary.exceptions;

import com.microservice_voluntary.dto.VoluntaryDTO;
import com.microservice_voluntary.entity.Voluntary;
import com.microservice_voluntary.repository.VoluntaryRepository;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoluntaryValidator {
  @Autowired
  private VoluntaryRepository voluntaryRepository;


  public void findById(Long id) throws Exception {
    if (!voluntaryRepository.findById(id).isPresent()) {
      throw new VoluntaryNotExistsException("No hay voluntario con el id " + id + " para mostrar");
    }
  }

  public void findByEmail(String email) throws Exception {
    if (!voluntaryRepository.findByEmail(email).isPresent())
      throw new VoluntaryNotExistsException("No existe voluntario con email " + email);
  }

  public void saveVoluntary(VoluntaryDTO voluntaryDTO) throws Exception {
    if (!voluntaryDTO.getPhone().matches("^(\\+\\d{1,3}\\s?)?(\\(?\\d{2,4}\\)?[-\\s]?)?\\d{4}[-\\s]?\\d{4}$"))
      throw new Exception("No corresponde a un formato de celular valido");

    if (voluntaryDTO.getName().trim().isEmpty())
      throw new NullPointerException("No hay nombre del voluntario");

    if (voluntaryRepository.findByEmail(voluntaryDTO.getEmail()).isPresent())
      throw new VoluntaryNotExistsException("Ya existe voluntario con email " + voluntaryDTO.getEmail());

    validateEmail(voluntaryDTO.getEmail());
  }

  public void validateEmail(String email) throws VoluntaryNotExistsException {
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      throw new VoluntaryNotExistsException("El correo no tiene un formato válido: " + email);
    }
  }

  public void updateVoluntary(Long idVoluntary, VoluntaryDTO voluntaryDTO) throws Exception {
    findById(idVoluntary);
    Voluntary voluntaryDDBB = voluntaryRepository.findById(idVoluntary).get();
    if(!voluntaryDDBB.getEmail().equals(voluntaryDTO.getEmail()))
      isRepeatEmail(voluntaryDTO.getEmail());
  }

  public void deleteVoluntary(Long idVoluntary) throws Exception {
    findById(idVoluntary);
  }

  public void isRepeatEmail(String email) throws VoluntaryNotExistsException {
    if (voluntaryRepository.findByEmail(email).isPresent())
      throw new VoluntaryNotExistsException("Ya existe un voluntario con ese email");
  }
}
