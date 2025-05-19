package org.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Nullable
  private String title;
  @Nullable
  private String description;
  @NotNull
  private BigDecimal amount;
  @NotNull
  private LocalDate dateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
