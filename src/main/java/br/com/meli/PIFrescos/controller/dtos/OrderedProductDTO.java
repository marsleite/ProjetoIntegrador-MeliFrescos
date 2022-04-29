package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.Section;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe OrderedProductDTO para devolver o Product com informações do respectivo Batch
 * @author Ana Preis
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductDTO {
    private Section section;
    private Integer productId;
    private List<OrderedBatchDTO> batchStock;

    public OrderedProductDTO(Batch batch, List<Batch> batches) {
        this.section = batch.getInboundOrder().getSection();
        this.productId = batch.getProduct().getProductId();
        this.batchStock = OrderedBatchDTO.convert(batches);
    }

    public static List<OrderedProductDTO> convertList(List<Batch> batches){
        return batches.stream().map(batch -> new OrderedProductDTO(batch, batches)).collect(Collectors.toList());
    }
}
