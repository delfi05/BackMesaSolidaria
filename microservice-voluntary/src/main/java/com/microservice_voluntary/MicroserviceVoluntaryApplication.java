package com.microservice_voluntary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class MicroserviceVoluntaryApplication {
  public static void main(String[] args) {
    SpringApplication.run(MicroserviceVoluntaryApplication.class, args);
  }
}
