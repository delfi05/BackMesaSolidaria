package com.microservice_manager;

import com.microservice_manager.Utils.LoadDatabase;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MicroserviceManagerApplication {
  @Autowired
  private LoadDatabase loadDatabase;

  public static void main(String[] args) {
    SpringApplication.run(MicroserviceManagerApplication.class, args);
  }

  @PostConstruct
  public void init() throws Exception {
    loadDatabase.loadAuthorities();
    loadDatabase.loadManager();
  }
}
