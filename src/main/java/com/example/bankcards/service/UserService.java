package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.dto.UserRs;
import com.example.dto.UsersPageRs;
import java.util.Optional;

public interface UserService {

  void deleteUser(Long id);

  UsersPageRs findAllUsers(Integer page, Integer size);

  UserRs findUser(Long id);

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

}
