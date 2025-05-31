package com.example.bankcards.service;


import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

  String generateAccessToken(UserDetails userDetails);

  String generateRefreshToken(UserDetails userDetails);

  String extractEmail(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

}
