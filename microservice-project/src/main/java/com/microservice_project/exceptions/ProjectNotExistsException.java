package com.microservice_project.exceptions;

public class ProjectNotExistsException extends Exception {
  public ProjectNotExistsException(String exception) {
    super(exception);
  }
}
