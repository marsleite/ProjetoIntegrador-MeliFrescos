package br.com.meli.PIFrescos.models.dto;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Felipe Myose
 * Criação do dto
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderDTO {

    private LocalDate orderDate;
    private Section section;
    private List<Batch> batchStock;

    public static InboundOrder convert(InboundOrderDTO dto) {
        return new InboundOrder(null, dto.getOrderDate(), dto.getSection(), dto.getBatchStock());
    }

}

