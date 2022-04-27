package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * @author Antonio Hugo
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderProductDTO {
    private Integer orderNumber;
    private List<Product> products;
}
