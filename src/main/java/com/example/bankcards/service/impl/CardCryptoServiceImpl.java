package com.example.bankcards.service.impl;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

import com.example.bankcards.config.CryptoProperties;
import com.example.bankcards.service.CardCryptoService;
import jakarta.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardCryptoServiceImpl implements CardCryptoService {

  private final CryptoProperties cryptoProperties;
  private final SecureRandom secureRandom = new SecureRandom();
  private SecretKey secretKey;

  @PostConstruct
  private void init() {
    byte[] keyBytes = hexStringToByteArray(cryptoProperties.getKey());
    secretKey = new SecretKeySpec(keyBytes, cryptoProperties.getAlgorithm());
  }

  @Override
  public String encrypt(String plainText) {
    try {
      byte[] iv = new byte[cryptoProperties.getIvLength()];
      secureRandom.nextBytes(iv);
      IvParameterSpec ivSpec = new IvParameterSpec(iv);

      Cipher cipher = Cipher.getInstance(cryptoProperties.getCipher());
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
      byte[] encrypted = cipher.doFinal(plainText.getBytes(Charset.forName(cryptoProperties.getEncoding())));

      byte[] ivAndCipher = new byte[iv.length + encrypted.length];
      System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
      System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

      return Base64.getEncoder().encodeToString(ivAndCipher);
    } catch (Exception e) {
      throw new RuntimeException("Encryption error", e);
    }
  }

  @Override
  public String decrypt(String encryptedText) {
    try {
      byte[] ivAndCipher = Base64.getDecoder().decode(encryptedText);
      byte[] iv = new byte[cryptoProperties.getIvLength()];
      byte[] encrypted = new byte[ivAndCipher.length - cryptoProperties.getIvLength()];

      System.arraycopy(ivAndCipher, 0, iv, 0, iv.length);
      System.arraycopy(ivAndCipher, iv.length, encrypted, 0, encrypted.length);

      Cipher cipher = Cipher.getInstance(cryptoProperties.getCipher());
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
      byte[] decrypted = cipher.doFinal(encrypted);

      return new String(decrypted, Charset.forName(cryptoProperties.getEncoding()));
    } catch (Exception e) {
      throw new RuntimeException("Decryption error", e);
    }
  }

  @Override
  public String hash(String cardNumber) {
    try {
      Mac mac = Mac.getInstance(cryptoProperties.getHashingAlgorithm());
      mac.init(secretKey);

      byte[] hashBytes = mac.doFinal(cardNumber.getBytes());

      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (Exception e) {
      throw new RuntimeException("Hashing error", e);
    }
  }

  @Override
  public String getMaskedCardNumber(@NonNull final String cardNumber) {
    final String visibleDigits = cardNumber.substring(cardNumber.length() - 4);
    return "**** **** **** " + visibleDigits;
  }
}
