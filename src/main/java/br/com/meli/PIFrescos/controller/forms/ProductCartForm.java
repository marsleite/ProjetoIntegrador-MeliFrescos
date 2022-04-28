package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductsCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductCartForm {

    private Integer batchId;
    private Integer quantity;

    private String productName; // podemos usar o productName no lugar do id

    /**
     * Converter a compra do usuario para o ProductsCart, o qual contem toda informaçao necessaria.
     * Muitas informaçoes são desconhecidas - buscar estes dados na camada de serviço.
      * @param productCartForm
     * @return productCart com valores vazios
     */
    public static ProductsCart convert(ProductCartForm productCartForm) {

        Batch batch = new Batch();
        batch.setBatchNumber(productCartForm.getBatchId());
        ProductsCart productsCart = new ProductsCart();

        productsCart.setBatch(batch);
        productsCart.setQuantity(productCartForm.getQuantity());

        return  productsCart;
    }
}
