package org.example.backend.Exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomSqlException extends RuntimeException{
  private String path;
  public CustomSqlException(String message, String path){
    super(message);this.path = path;
  }
}