package br.com.meli.PIFrescos.controller.dtos;

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
    private List<BatchDTO> batchStock;

    public static InboundOrder convert(InboundOrderDTO dto) {
        return new InboundOrder(null, dto.getOrderDate(), dto.getSection(),
                BatchDTO.convert(dto.getBatchStock()));
    }

}

