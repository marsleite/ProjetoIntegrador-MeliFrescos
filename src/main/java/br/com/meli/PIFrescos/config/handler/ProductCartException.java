package br.com.meli.PIFrescos.config.handler;


import br.com.meli.PIFrescos.config.ErrorFormsDto;
import br.com.meli.PIFrescos.models.ProductsCart;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductCartException extends RuntimeException{

    private List<ErrorFormsDto> errorFormsDtoList = new ArrayList<>();

    public void addError(String productName, String message){
        errorFormsDtoList.add(new ErrorFormsDto(productName, message));
    }

}
