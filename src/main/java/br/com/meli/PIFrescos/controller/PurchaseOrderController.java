package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.OrderProductDTO;
import br.com.meli.PIFrescos.controller.dtos.TotalPriceDTO;
import br.com.meli.PIFrescos.controller.forms.ProductCartForm;
import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/fresh-products/orders")
public class PurchaseOrderController {

    @Autowired
    PurchaseOrderService service;

    /**
     * Insere nova compra e retorna o valor total do pedido.
     *
     * @return TotalPriceDTO
     * @author Julio César Gama
     */

    @PostMapping("")
    public ResponseEntity<TotalPriceDTO> postOrder(@RequestBody PurchaseOrderForm purchaseOrderForm){

        PurchaseOrder order = PurchaseOrderForm.convertToEntity(purchaseOrderForm);
        PurchaseOrder savedOrder = service.save(order);

        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity<TotalPriceDTO>(new TotalPriceDTO(totalPrice), HttpStatus.CREATED);
    }

    /**
     * Este endpoint retorna todos os produtos de um pedido.
     * @param querytype
     * @return  OrderProductDTO
     * @author Antonio Hugo
     */
    @GetMapping("")
    public ResponseEntity<OrderProductDTO> getProductsByOrderId(@RequestParam Integer querytype) {
           List<Product> products = service.findProductsByOrderId(querytype);
        return ResponseEntity.ok().body(new OrderProductDTO(querytype, products));
    }
}
