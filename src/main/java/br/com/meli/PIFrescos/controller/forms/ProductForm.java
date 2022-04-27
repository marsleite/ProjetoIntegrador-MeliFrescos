package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Classe ProductForm para filtrar campos devolvidos na request
 * @author Juliano Alcione de Souza
 *
 * Validações
 * @author Julio César Gama
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {

    private Integer productId;

    @NotNull(message = "O nome do produto não pode ser nulo.")
    @Size(min = 5, max =  50, message = "O nome do produto deve conter entre 5 a 50 caracteres.")
    private String productName;
    private StorageType productType;

    @NotNull (message = "A descrição do produto não pode ser nulo.")
    @Size(min = 5, max =  255, message = "A descrição do produto deve conter entre 5 a 255 caracteres.")
    private String productDescription;

    public Product convert() {
        return new Product(productId, productName, productType, productDescription);
    }

}
