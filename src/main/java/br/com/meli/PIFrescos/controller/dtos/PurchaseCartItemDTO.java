package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.ProductsCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchaseCartItemDTO {

    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public static PurchaseCartItemDTO convert(ProductsCart productsCart) {
        return new PurchaseCartItemDTO().builder()
                .productId(productsCart.getBatch().getProduct().getProductId())
                .quantity(productsCart.getQuantity())
                .unitPrice(productsCart.getBatch().getUnitPrice())
                .build();
    }

}
