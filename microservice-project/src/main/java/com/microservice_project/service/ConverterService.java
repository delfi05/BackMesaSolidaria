package com.microservice_project.service;

import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class ConverterService {
  public String encode(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  public byte[] decode(String base64) {
    try {
      return Base64.getDecoder().decode(base64);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return null;
    }
  }
}
