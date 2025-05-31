package com.example.bankcards.mapper;

import com.example.bankcards.entity.User;
import com.example.dto.RegistrationRq;

public interface UserMapper {

  User toEntity(RegistrationRq rq);

}
