package org.example.backend.Controller;

import org.example.backend.Exception.CustomSqlException;
import org.example.backend.Exception.UsernameExistedException;
import org.example.backend.Dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(value = UsernameExistedException.class)
  public ResponseEntity<ApiErrorResponse> handleUsernameExisted(UsernameExistedException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorResponse.builder().code(400).message(ex.getMessage()).path(ex.getPath()).build());
  }

  @ExceptionHandler(CustomSqlException.class)
  public ResponseEntity<ApiErrorResponse> handleSqlException(CustomSqlException ex){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.builder().code(500).message(ex.getMessage()).path(ex.getPath()).build());
  }
}
