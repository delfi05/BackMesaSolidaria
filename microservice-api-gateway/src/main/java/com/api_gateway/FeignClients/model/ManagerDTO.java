package com.api_gateway.FeignClients.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDTO {

  private Long id_manager;
  private String name;
  private String lastName;
  private String email;
  private String password;


  // RESTABLECER CONTRASEÑA
  public ManagerDTO(String email) {
    this.email = email;
  }

  // LOGGGGGGGGGGG IN
  public ManagerDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }

  // Este es para la devolución cuando retorna el manager creado, actualizado ó borrado
  public ManagerDTO(String name, String lastName, String email) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
  }

  // Este constructor se utiliza para guardar un manager
  public ManagerDTO(String name, String lastName, String email, String password) {
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }
}
