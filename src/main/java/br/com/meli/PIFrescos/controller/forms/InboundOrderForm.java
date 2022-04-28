package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.controller.dtos.BatchDTO;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Felipe Myose, Antonio Hugo
 * Criação do dto, refatoração do DTO
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderForm {

    @NotEmpty(message = "orderDate field can't be empty")
    private LocalDate orderDate;
    private Section section;
    private List<BatchDTO> batchStock;

    public static InboundOrder convert(InboundOrderForm dto) {
        return new InboundOrder(null, dto.getOrderDate(), dto.getSection(),
                BatchDTO.convert(dto.getBatchStock()));
    }

}

