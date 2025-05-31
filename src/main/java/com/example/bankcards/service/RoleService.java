package com.example.bankcards.service;

import com.example.bankcards.entity.Role;
import java.util.Optional;

public interface RoleService {

  Optional<Role> findByName(String name);

}
