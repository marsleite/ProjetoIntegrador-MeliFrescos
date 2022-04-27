package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Felipe Myose
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDTO {

  private Integer batchNumber;
  private Float currentTemperature;
  private Float minimumTemperature;
  private Integer initialQuantity;
  private Integer currentQuantity;
  private LocalDate manufacturingDate;
  private LocalDate dueDate;

  private Integer productId;

  public static Batch convert(BatchDTO dto) {
    return Batch.builder()
            .batchNumber(dto.getBatchNumber())
            .currentTemperature(dto.getCurrentTemperature())
            .minimumTemperature(dto.getMinimumTemperature())
            .initialQuantity(dto.getInitialQuantity())
            .currentQuantity(dto.getCurrentQuantity())
            .manufacturingDate(dto.getManufacturingDate())
            .dueDate(dto.getDueDate())
            .product(new Product(dto.getProductId(),null,null,null))
            .build();
  }

  public static List<Batch> convert(List<BatchDTO> dtos) {
    return dtos.stream().map(batchDTO -> convert(batchDTO)).collect(Collectors.toList());
  }
}
