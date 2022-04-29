package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Batch;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe OrderedProductDTO para devolver o Product com informações do respectivo Batch
 * @author Ana Preis
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedBatchDTO {
    private Integer batchNumber;
    private Integer currentQuantity;
    private LocalDate dueDate;

    public OrderedBatchDTO(Batch batch) {
        this.batchNumber = batch.getBatchNumber();
        this.currentQuantity = batch.getCurrentQuantity();
        this.dueDate = batch.getDueDate();
    }

    public static OrderedBatchDTO convert(Batch batch){
        return new OrderedBatchDTO(batch);
    }

    public static List<OrderedBatchDTO> convert(List<Batch> batches){
        return batches.stream().map(OrderedBatchDTO::new).collect(Collectors.toList());
    }
}
