package com.example.bankcards.controller;

import com.example.api.UserApi;
import com.example.bankcards.service.UserService;
import com.example.dto.UserRs;
import com.example.dto.UsersPageRs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<Void> deleteUser(final Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UsersPageRs> findAllUsers(final Integer page, final Integer size) {
    return ResponseEntity.ok(userService.findAllUsers(page, size));
  }

  @Override
  public ResponseEntity<UserRs> findUserById(final Long id) {
    return ResponseEntity.ok(userService.findUser(id));
  }
}
