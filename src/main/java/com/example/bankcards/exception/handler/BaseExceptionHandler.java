package com.example.bankcards.exception.handler;

import com.example.dto.ErrorRs;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

  protected ResponseEntity<ErrorRs> handleGenericException(HttpStatus httpStatus, Throwable ex){
    ErrorRs rs = new ErrorRs();

    rs.setErrorCode(httpStatus.value());
    rs.setErrorDate(LocalDateTime.now().toString());
    rs.setErrorDescription(ex.getMessage());

    return ResponseEntity.status(httpStatus).body(rs);
  }
}
