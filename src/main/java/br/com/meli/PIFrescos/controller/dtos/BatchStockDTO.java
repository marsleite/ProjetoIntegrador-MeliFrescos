package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ana Preis
 * DTO para o requisito 05, onde precisamos de uma lista de Batches por setor e ordenados por ordem de vencimento.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchStockDTO {

    private Integer batchNumber;
    private Integer productId;
    private StorageType productType;
    private LocalDate dueDate;
    private Integer quantity;

    public BatchStockDTO(Batch batch) {
        this.batchNumber = batch.getBatchNumber();
        this.productId = batch.getProduct().getProductId();
        this.productType = batch.getInboundOrder().getSection().getStorageType();
        this.dueDate = batch.getDueDate();
        this.quantity = batch.getCurrentQuantity();
    }

    public static List<BatchStockDTO> convert(List<Batch> batches) {
        return batches.stream().map(BatchStockDTO::new).collect(Collectors.toList());
    }
}
