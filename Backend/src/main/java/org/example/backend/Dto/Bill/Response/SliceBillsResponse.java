package org.example.backend.Dto.Bill.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.Dto.Bill.BillDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SliceBillsResponse {
  private List<BillDTO> content;
  private boolean hasNext;
  private boolean hasPrevious;
  private int pageNumber;
  private int pageSize;
}
