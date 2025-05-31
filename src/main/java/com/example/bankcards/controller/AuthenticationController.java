package com.example.bankcards.controller;

import com.example.api.AuthenticationApi;
import com.example.bankcards.service.AuthenticationService;
import com.example.dto.AuthenticationRq;
import com.example.dto.AuthenticationRs;
import com.example.dto.RegistrationRq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

  private final AuthenticationService authenticationService;

  @Override
  public ResponseEntity<AuthenticationRs> login(final AuthenticationRq authenticationRq) {
    return ResponseEntity.ok(authenticationService.login(authenticationRq));
  }

  @Override
  public ResponseEntity<AuthenticationRs> register(final RegistrationRq registrationRq) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(registrationRq));
  }
}
