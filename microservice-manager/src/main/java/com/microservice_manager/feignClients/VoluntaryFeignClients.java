package com.microservice_manager.feignClients;

import com.microservice_manager.feignClients.model.VoluntaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microservice-voluntary")
public interface VoluntaryFeignClients {

  @GetMapping("/api/voluntaries")
  List<VoluntaryDTO> getAll();

  @GetMapping("/api/voluntaries/{id_voluntary}")
  VoluntaryDTO getVoluntaryByID(@PathVariable Long id_voluntary);

  @GetMapping("/api/voluntaries/findByEmail/{email}")
  VoluntaryDTO getVoluntaryByEmail(@PathVariable String email);

  @PostMapping("/api/voluntaries")
  VoluntaryDTO saveVoluntary(@RequestBody VoluntaryDTO voluntaryDTO);

  @PutMapping("/api/voluntaries/updateVoluntary/{id_voluntary}")
  VoluntaryDTO updateVoluntary(@PathVariable Long id_voluntary, @RequestBody VoluntaryDTO voluntaryDTO);

  @DeleteMapping("/api/voluntaries/deleteVoluntary/{id_voluntary}")
  VoluntaryDTO deleteVoluntary(@PathVariable Long id_voluntary);
}
