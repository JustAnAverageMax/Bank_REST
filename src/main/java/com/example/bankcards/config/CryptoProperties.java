package com.example.bankcards.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
public class CryptoProperties {

  @Value("${crypto.algorithm}")
  private String algorithm;

  @Value("${crypto.cipher}")
  private String cipher;

  @Value("${crypto.key}")
  private String key;

  @Value("${crypto.ivLength}")
  private int ivLength;

  @Value("${crypto.encoding}")
  private String encoding;

  @Value("${crypto.hash.name}")
  private String hashingAlgorithm;
}
