package br.com.meli.PIFrescos.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class TotalPriceDTO {

    private BigDecimal totalPrice;

}
