package com.microservice_manager.feignClients.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoluntaryDTO {

  private Long id_voluntary;
  private String name;
  private String lastName;
  private String email;
  private String phone;

  public VoluntaryDTO(String name, String lastName, String email, String phone) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }
}