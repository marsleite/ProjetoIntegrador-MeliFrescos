package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductWarehousesDTO;
import br.com.meli.PIFrescos.service.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fresh-products/warehouse")
public class WarehouseController {

    @Autowired
    private BatchServiceImpl service;

    /**
     * Este endpoint retorna a quantidade de produto por warehouse.
     * @return ProductWarehousesDTO
     * @author Juliano Alcione de Souza
     */

    @GetMapping
    public ResponseEntity<ProductWarehousesDTO> getPurchaseOrder(@RequestParam Integer productId) {
        ProductWarehousesDTO quantityProductByWarehouse = service.getQuantityProductByWarehouse(productId);
        return ResponseEntity.ok(quantityProductByWarehouse);
    }
}
