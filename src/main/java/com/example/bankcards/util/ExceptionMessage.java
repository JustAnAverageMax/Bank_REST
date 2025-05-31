package com.example.bankcards.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
  CARD_ALREADY_EXISTS("Card registration failed. User already has card with provided number"),
  USER_ALREADY_EXISTS("User registration failed. User with provided credentials already exists"),
  ENTITY_NOT_FOUND("%s with provided credentials not found"),
  INVALID_CREDENTIALS("Invalid credentials provided"),
  USERNAME_NOT_FOUND("User with email '%s' not found");


  private final String message;

  public String format(Object... args){
    return String.format(message, args);
  }

}
