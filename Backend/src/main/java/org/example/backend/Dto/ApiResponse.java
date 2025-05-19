package org.example.backend.Dto;

import lombok.*;

@Data
@NoArgsConstructor
public class ApiResponse {
  private Object data;
  private String message;
  private int statusCode;
  public ApiResponse(Object data,String message, int statusCode){
    this.data = data;
    this.message = message;
    this.statusCode = statusCode;
  }
}
