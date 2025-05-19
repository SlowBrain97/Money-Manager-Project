package org.example.backend.Controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.Dto.Bill.Request.SaveBillsForMonthsRequest;
import org.example.backend.Dto.Bill.Response.SliceBillsResponse;
import org.example.backend.Service.BillServiceImpl;
import org.example.backend.Dto.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bill")
public class BillController {
  private final BillServiceImpl billService;

  @PostMapping("/save")
  public ResponseEntity<ApiResponse> saveBillForMonth(@RequestBody List<SaveBillsForMonthsRequest> bills){
    billService.saveAllBill(bills);
    return ResponseEntity.ok(new ApiResponse(null,"Add bills successfully", 201));
  }
  @GetMapping("/find/{year}")
  public ResponseEntity<ApiResponse> getBillsForYear (@PathVariable int year, Pageable pageable){
    SliceBillsResponse billsResponse = billService.getBillsByYear(year,pageable);
    return ResponseEntity.ok(new ApiResponse(billsResponse,"Successfully", 200));
  }


  @GetMapping("/find/{year}/{month}")
  public ResponseEntity<ApiResponse> getBillsForYearAndMonth(@PathVariable int year, @PathVariable int month,Pageable pageable){
    SliceBillsResponse billsResponse = billService.getBillsByYearAndMonth(year,month,pageable);
    return ResponseEntity.ok(new ApiResponse(billsResponse,"Successfully", 200));
  }

  @GetMapping("/totalSpending/{year}/{month}")
  public ResponseEntity<ApiResponse> getTotalSpendingByMonth(@PathVariable int year,@PathVariable int month){
    BigDecimal spending = billService.getTotalSpendingByMonth(year,month);
    return ResponseEntity.ok(new ApiResponse(spending,"Successfully", 200));
  }

  @GetMapping("/totalSpending/{year}")
  public ResponseEntity<ApiResponse> getTotalSpendingByYear(@PathVariable int year){
    BigDecimal spending = billService.getTotalSpendingByYear(year);
    return ResponseEntity.ok(new ApiResponse(spending,"Successfully", 200));
  }
}
