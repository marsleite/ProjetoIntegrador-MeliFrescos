package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe ProductForm para filtrar campos devolvidos na request
 * @author Juliano Alcione de Souza
 */

@Getter
@Setter
public class ProductDto {
    private Integer productId;
    private String productName;
    private StorageType productType;
    private String productDescription;

    public ProductDto(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productType = product.getProductType();
        this.productDescription = product.getProductDescription();
    }

    public static List<ProductDto> convertList(List<Product> products){
        return products.stream().map(ProductDto::new).collect(Collectors.toList());
    }
}
