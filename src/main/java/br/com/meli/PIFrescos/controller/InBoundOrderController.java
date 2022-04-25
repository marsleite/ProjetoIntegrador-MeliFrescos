package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.dto.InboundOrderDTO;
import br.com.meli.PIFrescos.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Busca todos os InboundOrders existentes
 * para armazenar.
 * @return List<inboundOrder>
 * @author Julio César Gama
 */

@RestController
@RequestMapping("/fresh-products/inboundorder/")
public class InBoundOrderController {

    @Autowired
    InboundOrderService service;

    @GetMapping("")
    public ResponseEntity<List<InboundOrder>> getInboundOrders(){
        return ResponseEntity.ok(service.getAll());
    }


    /**
     * @author Ana Preis
     */
    @PostMapping("")
    public ResponseEntity<List<Batch>> postInboundOrders(@RequestBody InboundOrderDTO orderDTO){
        InboundOrder order = InboundOrderDTO.convert(orderDTO);
        InboundOrder savedOrder = service.save(order);

        return new ResponseEntity(savedOrder.getBatchStock(), HttpStatus.CREATED);
    }
}
