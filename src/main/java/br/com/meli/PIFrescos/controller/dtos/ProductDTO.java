package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe ProductDTO para filtrar campos devolvidos na request
 * @author Juliano Alcione de Souza
 *
 * Refactor:
 * @author Julio César Gama
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String productName;
    private StorageType productType;
    private String productDescription;

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productType = product.getProductType();
        this.productDescription = product.getProductDescription();
    }

    public static List<ProductDTO> convertList(List<Product> products){
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());
    }
}
