package com.example.bankcards.exception;

public class CardAlreadyExistsException extends EntityAlreadyExistsException {

  public CardAlreadyExistsException(String message) {
    super(message);
  }
}
