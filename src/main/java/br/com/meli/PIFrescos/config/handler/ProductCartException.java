package br.com.meli.PIFrescos.config.handler;


import br.com.meli.PIFrescos.config.ErrorFormsDto;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.repository.ProductRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductCartException extends RuntimeException{

    @Autowired
    private ProductRepository productRepository;

    private List<ErrorFormsDto> errorFormsDtoList = new ArrayList<>();

    public ProductCartException(List<ProductsCart> invalidProductList) {
        invalidProductList.forEach(item -> {
            if(item.getBatch().getProduct() != null){
                errorFormsDtoList.add(new ErrorFormsDto(item.getBatch().getProduct().getProductName(), "Insuficient quantity of product on batch"));
            }
        });
    }
}
