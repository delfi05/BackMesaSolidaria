package com.api_gateway.Controller;

import com.api_gateway.FeignClients.ManagerFeignClients;
import com.api_gateway.FeignClients.model.ManagerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ManagerController {

  private final ManagerFeignClients managerFeignClients;

  @PostMapping("/register")
  public ResponseEntity<String> save(@RequestBody ManagerDTO managerDTO) {
    return managerFeignClients.saveManager(managerDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody ManagerDTO managerDTO) {
    return managerFeignClients.loginManager(managerDTO);
  }

  @PostMapping("/forgotPassword")
  public ResponseEntity<String> forgotPasswordManager(@RequestBody ManagerDTO managerDTO) {
    return managerFeignClients.forgotPasswordManager(managerDTO);
  }

  @GetMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    return managerFeignClients.logoutManager(token);
  }
}