package com.microservice_manager.Utils;

import com.microservice_manager.dto.ManagerDTO;
import com.microservice_manager.entity.Authority;
import com.microservice_manager.repository.AuthorityRepository;
import com.microservice_manager.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase {
  @Autowired
  private ManagerService managerService;
  @Autowired
  private AuthorityRepository authorityRepository;

  // dar de alta manager
  public void loadManager() throws Exception {

    try {
      ManagerDTO managerAdmin = new ManagerDTO("admin", "administrador", "admin@gmail.com", "administrador123");
      if (managerService.findAll().isEmpty()) {
        managerService.saveManager(managerAdmin);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  // dar de alta Autoridades
  public void loadAuthorities() throws Exception {
    Authority MANAGER = new Authority("MANAGER");
    try {
      if (authorityRepository.findAll().isEmpty()) {
        authorityRepository.save(MANAGER);
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}