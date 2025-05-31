package com.example.bankcards.exception;

public class UserAlreadyExistsException extends EntityAlreadyExistsException {

  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
