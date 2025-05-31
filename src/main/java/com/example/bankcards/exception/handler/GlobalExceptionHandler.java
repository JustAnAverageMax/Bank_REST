package com.example.bankcards.exception.handler;

import com.example.bankcards.exception.EntityAlreadyExistsException;
import com.example.dto.ErrorRs;
import com.example.dto.FieldViolationRs;
import com.example.dto.ValidationErrorRs;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

  @ExceptionHandler(EntityAlreadyExistsException.class)
  public ResponseEntity<ErrorRs> handleEntityAlreadyExistsExceptions(EntityAlreadyExistsException ex){
    return handleGenericException(HttpStatus.CONFLICT, ex);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorRs> handleBadCredentialsException(BadCredentialsException ex){
    return handleGenericException(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorRs> handleEntityNotFoundException(EntityNotFoundException ex){
    return handleGenericException(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorRs> handleValidationErrors(MethodArgumentNotValidException ex){
    List<FieldViolationRs> violations = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldViolationRs(error.getField(), error.getDefaultMessage()))
        .toList();

    return ResponseEntity.badRequest().body(new ValidationErrorRs(violations));
  }

}
