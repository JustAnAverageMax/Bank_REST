package com.example.bankcards.exception.handler;

import com.example.dto.ErrorRs;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler extends BaseExceptionHandler {

  @ExceptionHandler({
      SignatureException.class,
      MalformedJwtException.class,
      ExpiredJwtException.class,
      UnsupportedJwtException.class,
      AuthenticationCredentialsNotFoundException.class
  })
  public ResponseEntity<ErrorRs> handleJwtAuthExceptions(Exception ex) {
    return handleGenericException(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler({AccessDeniedException.class, InsufficientAuthenticationException.class})
  public ResponseEntity<ErrorRs> handleAccessDenied(Exception ex) {
    return handleGenericException(HttpStatus.FORBIDDEN, ex);
  }

}
