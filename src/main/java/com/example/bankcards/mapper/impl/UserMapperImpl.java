package com.example.bankcards.mapper.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.dto.RegistrationRq;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

  private final PasswordEncoder passwordEncoder;

  @Override
  public User toEntity(@NonNull final RegistrationRq rq) {
    final User user = new User();

    user.setFirstName(rq.getFirstName());
    user.setLastName(rq.getLastName());
    user.setEmail(rq.getEmail());
    user.setPassword(passwordEncoder.encode(rq.getPassword()));

    return user;
  }
}
