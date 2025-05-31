package com.example.bankcards.service;

import com.example.dto.AuthenticationRq;
import com.example.dto.AuthenticationRs;
import com.example.dto.RegistrationRq;

public interface AuthenticationService {

  AuthenticationRs login(AuthenticationRq rq);

  AuthenticationRs register(RegistrationRq rq);

}
