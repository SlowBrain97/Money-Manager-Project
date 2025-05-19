package org.example.backend.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.Dto.Bill.BillDTO;
import org.example.backend.Dto.Bill.Response.SliceBillsResponse;
import org.example.backend.Entity.Bill;
import org.example.backend.Exception.CustomSqlException;
import org.example.backend.Mapper.BillMapper;
import org.example.backend.Dto.Bill.Request.SaveBillsForMonthsRequest;
import org.example.backend.Repository.BillRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements iBillService{
  private final BillRepository billRepository;
  private final BillMapper mapper;
  private final EntityManagerFactory entityManagerFactory;

  public void saveAllBill (List<SaveBillsForMonthsRequest> billsOfMonths){
    List<Bill> bills = mapper.mapToEntityFromRequest(billsOfMonths);
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try{
      entityManager.getTransaction().begin();
      int batchSize = 50;
      for (int i = 0; i < bills.size(); i++){
        entityManager.persist(bills.get(i));

        if (i > 0 && i % batchSize == 0){
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.getTransaction().commit();
    }
    catch (Exception exception){
        if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
        log.error("Database error: {}", exception.getMessage(), exception);
        throw new CustomSqlException("Occur error in querying database","/api/v1/bill/create");
    }
    finally {
      entityManager.close();
    }
  }


  public SliceBillsResponse getBillsByYear(int year , Pageable pageable){
    try {
      Slice<Bill> bills = billRepository.findAllByYear(year, pageable);
      return mapper.mapSliceToDto(bills);
    }
    catch (Exception ex){
      throw new CustomSqlException("Had error in querying database" ,"/api/v1/bill/year");
    }
  }
  public SliceBillsResponse getBillsByYearAndMonth(int year, int month, Pageable pageable){
    try {
      Slice<Bill> bills = billRepository.findAllByYearAndMonth(year,month,pageable);
      return mapper.mapSliceToDto(bills);
    }
    catch (Exception ex){
      throw new CustomSqlException("Had error in querying database" ,"/api/v1/bill/year/month");
    }
  }

  public BigDecimal getTotalSpendingByMonth(int year, int month) {
    try {
      return billRepository.totalOfYearAndMonth(year,month);
    }
    catch (Exception ex){
      throw new CustomSqlException("Had error in querying database" ,"/api/v1/bill/totalSpending/year/month");
    }
  }

  public BigDecimal getTotalSpendingByYear(int year) {
    try {
      return billRepository.totalOfYear(year);
    }
    catch (Exception ex){
      throw new CustomSqlException("Had error in querying database" ,"/api/v1/bill/totalSpending/year");
    }

  }
}
