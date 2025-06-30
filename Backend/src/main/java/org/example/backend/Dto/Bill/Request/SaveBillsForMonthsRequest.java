package org.example.backend.Dto.Bill.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveBillsForMonthsRequest {
  @Nullable
  private Long userId;
  @Nullable
  private String title;
  @Nullable
  private String description  ;
  @NotNull
  private BigDecimal amount;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateTime;
}
