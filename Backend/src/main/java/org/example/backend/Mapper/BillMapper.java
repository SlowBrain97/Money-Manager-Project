package org.example.backend.Mapper;

import org.example.backend.Dto.Bill.Response.SliceBillsResponse;
import org.example.backend.Entity.Bill;
import org.example.backend.Dto.Bill.BillDTO;
import org.example.backend.Dto.Bill.Request.SaveBillsForMonthsRequest;
import org.mapstruct.*;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = BaseMapper.class)
public interface BillMapper extends BaseMapper<Bill, BillDTO> {
  List<Bill> mapToEntityFromRequest(List<SaveBillsForMonthsRequest> request);
  default SliceBillsResponse mapSliceToDto(Slice<Bill> billSlice){
    List<BillDTO> billDTOS = this.mapToListDto(billSlice.getContent());
    SliceBillsResponse billsResponse = new SliceBillsResponse();
    billsResponse.setContent(billDTOS);
    billsResponse.setHasPrevious(billSlice.hasPrevious());
    billsResponse.setHasNext(billSlice.hasNext());
    billsResponse.setPageNumber(billSlice.getNumber());
    billsResponse.setPageSize(billSlice.getSize());
    return billsResponse;
  };
  @Override
  @Mapping(target = "description", ignore = true)
  @Mapping(target = "user", ignore = true)
  Bill mapToEntity(BillDTO dto);

  @Override
  BillDTO mapToDto(Bill entity);

  @Override
  void updateEnity(BillDTO dto, @MappingTarget Bill entity);

  @Override
  List<Bill> mapToListEntity(List<BillDTO> dto);

  @Override
  List<BillDTO> mapToListDto(List<Bill> entity);
}
