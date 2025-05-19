package org.example.backend.Dto.Bill;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillDTO {
  private long id;

  private String title;

  private String describe;

  private BigDecimal amount;

  private LocalDate createAt;

}
