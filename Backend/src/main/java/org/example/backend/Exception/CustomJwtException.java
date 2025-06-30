package org.example.backend.Exception;

import io.jsonwebtoken.JwtException;

public class CustomJwtException extends JwtException {
  public CustomJwtException(String message) {
    super(message);
  }
}
