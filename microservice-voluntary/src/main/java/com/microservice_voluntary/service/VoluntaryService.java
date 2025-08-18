package com.microservice_voluntary.service;

import com.microservice_voluntary.dto.VoluntaryDTO;
import com.microservice_voluntary.entity.Voluntary;
import com.microservice_voluntary.exceptions.VoluntaryValidator;
import com.microservice_voluntary.repository.VoluntaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VoluntaryService {

  @Autowired
  private VoluntaryRepository voluntaryRepository;
  @Autowired
  private VoluntaryValidator voluntaryValidator;
  @Autowired
  private EmailService emailService;

  @Transactional(readOnly = true)
  public List<VoluntaryDTO> findAll() throws Exception {
    try {
      List<Voluntary> voluntaries = voluntaryRepository.findAll();
      List<VoluntaryDTO> listVoluntaryDTO = new ArrayList<>();
      for (Voluntary v : voluntaries) {
        VoluntaryDTO voluntaryDTO = new VoluntaryDTO(v.getName(), v.getLastName(), v.getEmail(), v.getPhone());
        voluntaryDTO.setId_voluntary(v.getId_voluntary());
        listVoluntaryDTO.add(voluntaryDTO);
      }
      return listVoluntaryDTO;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO findById(Long id) throws Exception {
    voluntaryValidator.findById(id);
    try {
      Voluntary v = voluntaryRepository.findById(id).get();
      VoluntaryDTO voluntaryDTO = new VoluntaryDTO(v.getName(), v.getLastName(), v.getEmail(), v.getPhone());
      voluntaryDTO.setId_voluntary(v.getId_voluntary());
      return voluntaryDTO;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO findByEmail(String email) throws Exception {
    voluntaryValidator.findByEmail(email);
    try {
      Optional<VoluntaryDTO> optionalManager = voluntaryRepository.findByEmail(email);
      VoluntaryDTO v = optionalManager.get();
      return new VoluntaryDTO(v.getName(), v.getLastName(), v.getEmail(), v.getPhone());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO saveVoluntary(VoluntaryDTO voluntaryDTO) throws Exception {
    voluntaryValidator.saveVoluntary(voluntaryDTO);
    try {
      Voluntary voluntary = mapToVoluntary(voluntaryDTO);
      voluntaryRepository.save(voluntary);
      emailService.sendEmail(voluntaryDTO);

      return new VoluntaryDTO(voluntary.getName(), voluntary.getLastName(), voluntary.getEmail(), voluntary.getPhone());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO updateVoluntary(Long id_voluntary, VoluntaryDTO voluntaryDTO) throws Exception {
    voluntaryValidator.updateVoluntary(id_voluntary, voluntaryDTO);
    try {
      if (voluntaryRepository.existsById(id_voluntary)) {
        Voluntary voluntary = mapToVoluntary(voluntaryDTO);
        voluntary.setId_voluntary(id_voluntary);
        voluntaryRepository.save(voluntary);
        return new VoluntaryDTO(voluntary.getName(), voluntary.getLastName(), voluntary.getEmail(), voluntary.getPhone());
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return null;
  }

  public VoluntaryDTO deleteVoluntary(Long id_voluntary) throws Exception {
    voluntaryValidator.deleteVoluntary(id_voluntary);
    try {
      if (voluntaryRepository.existsById(id_voluntary)) {
        Voluntary voluntary = voluntaryRepository.findById(id_voluntary).get();
        voluntaryRepository.deleteById(id_voluntary);

        return new VoluntaryDTO(voluntary.getName(), voluntary.getLastName(), voluntary.getEmail(), voluntary.getPhone());
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return null;
  }
  private Voluntary mapToVoluntary(VoluntaryDTO voluntaryDTO) {
    Voluntary voluntary = new Voluntary();
    voluntary.setName(voluntaryDTO.getName().toLowerCase());
    voluntary.setLastName(voluntaryDTO.getLastName().toLowerCase());
    voluntary.setEmail(voluntaryDTO.getEmail().toLowerCase());
    voluntary.setPhone(voluntaryDTO.getPhone());
    return voluntary;
  }
}