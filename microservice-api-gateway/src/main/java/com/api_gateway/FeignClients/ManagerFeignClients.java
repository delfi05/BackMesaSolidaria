package com.api_gateway.FeignClients;

import com.api_gateway.FeignClients.model.ManagerDTO;
import jakarta.ws.rs.GET;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "microservice-manager")
public interface ManagerFeignClients {

  @PostMapping("/api/managers/register")
  ResponseEntity<String> saveManager(@RequestBody ManagerDTO managerDTO);

  @PostMapping("/api/managers/login")
  ResponseEntity<String> loginManager(@RequestBody ManagerDTO managerDTO);

  @PostMapping("/api/managers/validateToken")
  ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader);

  @PostMapping("/api/managers/forgotPassword")
  ResponseEntity<String> forgotPasswordManager(@RequestBody ManagerDTO managerDTO);

  @PostMapping("/api/managers/isTokenBlacklisted")
  ResponseEntity<Boolean> isTokenBlacklisted(@RequestHeader("Authorization") String token);

  @GetMapping("/api/managers/logoutManager")
  ResponseEntity<String> logoutManager(@RequestHeader("Authorization") String token);
}
