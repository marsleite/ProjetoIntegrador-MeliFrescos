package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Classe ProductForm para filtrar campos vindos na request
 * @author Juliano Alcione de Souza
 */

@Getter
@Setter
public class ProductForm {
    private Integer productId;
    @NotBlank @Size(min = 5, max = 30)
    private String name;

    private StorageType productType;
    @NotBlank  @Size(min = 5, max = 30)
    private String productDescription;

    public Product convert() {
        return new Product(null, name, productType, productDescription);
    }
}
