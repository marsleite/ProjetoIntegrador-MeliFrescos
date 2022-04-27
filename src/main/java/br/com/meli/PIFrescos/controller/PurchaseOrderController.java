package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.TotalPriceDTO;
import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/orders")
public class PurchaseOrderController {

    @Autowired
    PurchaseOrderService service;

    /**
     * Insere nova compra e retorna o valor total do pedido.
     * @return TotalPriceDTO
     * @author Julio César Gama
     */

    @PostMapping("")
    public ResponseEntity<TotalPriceDTO> postOrder(@RequestBody PurchaseOrderForm form){

        PurchaseOrder order = form.convertToEntity();
        PurchaseOrder savedOrder = service.save(order);

        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity<TotalPriceDTO>(new TotalPriceDTO(totalPrice), HttpStatus.CREATED);
    }
}
