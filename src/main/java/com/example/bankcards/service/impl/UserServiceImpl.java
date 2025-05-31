package com.example.bankcards.service.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import com.example.dto.UserRs;
import com.example.dto.UsersPageRs;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void deleteUser(Long id) {

  }

  @Override
  public UsersPageRs findAllUsers(Integer page, Integer size) {
    return null;
  }

  @Override
  public UserRs findUser(Long id) {
    return null;
  }

  @Override
  public boolean existsByEmail(@NonNull final String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public Optional<User> findByEmail(@NonNull final String email) {
    return userRepository.findByEmail(email);
  }
}
