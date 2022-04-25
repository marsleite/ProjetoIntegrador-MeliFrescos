package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.service.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Busca todos os InboundOrders existentes
 * para armazenar.
 * @return List<inboundOrder>
 * @author Julio César Gama
 */

@RestController
@RequestMapping("/api/v1/fresh-products/inboundorder/")
public class InBoundOrderController {

    @Autowired
    InboundOrderService service;

    @GetMapping("")
    public ResponseEntity<List<InboundOrder>> getInboundOrders(){
        return ResponseEntity.ok(service.getAll());
    }
}
