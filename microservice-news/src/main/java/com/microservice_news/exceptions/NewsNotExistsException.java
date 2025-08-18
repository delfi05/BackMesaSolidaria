package com.microservice_news.exceptions;

public class NewsNotExistsException extends Exception {
  public NewsNotExistsException(String exception) {
    super(exception);
  }
}
