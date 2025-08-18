package com.microservice_manager.exceptions;

public class ManagerAlreadyExistsException extends Exception {
  public ManagerAlreadyExistsException(String exception) {
    super(exception);
  }
}
