package org.example.backend.Repository;

import org.example.backend.Entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
  @Query(value = "select * from bill b where YEAR(b.date_time) = :year", nativeQuery = true)
  Slice<Bill> findAllByYear(@Param("year") int year, Pageable pageable);

  @Query(value = "select * from bill b where YEAR(b.date_time) = :year AND MONTH(b.date_time) = :month", nativeQuery = true)
  Slice<Bill> findAllByYearAndMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);


  @Query(value = "select SUM(b.amount) from bill b GROUP BY YEAR(b.date_time) = :year", nativeQuery = true)
  BigDecimal totalOfYear (@Param("year") int year);


  @Query(value = "select SUM(b.amount) from bill b GROUP BY YEAR(b.date_time) = :year,MONTH(b.date_time) = :month", nativeQuery = true)
  BigDecimal totalOfYearAndMonth (@Param("year") int year,@Param("month") int month);
}
