package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AuthenticationService;
import com.example.bankcards.service.JwtService;
import com.example.bankcards.service.RoleService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ExceptionMessage;
import com.example.dto.AuthenticationRq;
import com.example.dto.AuthenticationRs;
import com.example.dto.RegistrationRq;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserService userService;
  private final JwtService jwtService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final UserRepository userRepository;

  @Override
  public AuthenticationRs login(@NonNull final AuthenticationRq rq) {
    User user = userService.findByEmail(rq.getEmail()).orElseThrow(() -> {
      log.error("User with email '{}' not found", rq.getEmail());
      return new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.format(User.class.getSimpleName()));
    });

    if (!passwordEncoder.matches(rq.getPassword(), user.getPassword())) {
      log.error("Invalid password provided for user with id '{}'", user.getId());

      throw new BadCredentialsException(ExceptionMessage.INVALID_CREDENTIALS.getMessage());
    }

    final String accessToken = jwtService.generateAccessToken(user);
    final String refreshToken = jwtService.generateRefreshToken(user);

    return new AuthenticationRs(accessToken, refreshToken);
  }

  @Transactional
  @Override
  public AuthenticationRs register(@NonNull final RegistrationRq rq) {
    if (userService.existsByEmail(rq.getEmail())) {
      log.error("User with email '{}' already exists", rq.getEmail());
      throw new UserAlreadyExistsException(ExceptionMessage.USER_ALREADY_EXISTS.getMessage());
    }

    User user = userMapper.toEntity(rq);

    // TODO:
    // Improve role system
    Role role = roleService.findByName("USER").orElseThrow(() -> {
      log.error("Role with name User not found");
      return new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND.format(Role.class.getSimpleName()));
    });

    user.getRoles().add(role);
    userRepository.save(user);
    log.info("User with email '{}' successfully saved", user.getEmail());

    final String accessToken = jwtService.generateAccessToken(user);
    final String refreshToken = jwtService.generateRefreshToken(user);

    return new AuthenticationRs(accessToken, refreshToken);
  }
}
