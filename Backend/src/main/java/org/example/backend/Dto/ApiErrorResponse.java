package org.example.backend.Dto;

import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
  private String message;
  private int code;
  private String path;
  private LocalDateTime timeStamp;

  @PrePersist
  private void setTimeStamp(){
    this.timeStamp = LocalDateTime.now();
  }
}
