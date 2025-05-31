package com.example.bankcards.service;

public interface CardCryptoService {

  String encrypt(String plainText);

  String decrypt(String encryptedText);

  String hash(String plainText);

  String getMaskedCardNumber(String cardNumber);

}
