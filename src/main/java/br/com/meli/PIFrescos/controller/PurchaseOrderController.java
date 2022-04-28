package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.config.security.TokenService;
import br.com.meli.PIFrescos.controller.dtos.OrderProductDTO;
import br.com.meli.PIFrescos.controller.dtos.TotalPriceDTO;
import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;

import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.models.Product;
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
    private PurchaseOrderService service;

    @Autowired
    private TokenService tokenService;

    @Autowired
    TokenService tokenService;

    /**
     * Insere nova compra e retorna o valor total do pedido.
     *
     * @return TotalPriceDTO
     * @author Julio César Gama
     */

    @PostMapping("")
    public ResponseEntity<TotalPriceDTO> postOrder(@RequestBody PurchaseOrderForm purchaseOrderForm){

        PurchaseOrder order = purchaseOrderForm.convertToEntity(tokenService);
        PurchaseOrder savedOrder = service.save(order);

        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity<TotalPriceDTO>(new TotalPriceDTO(totalPrice), HttpStatus.CREATED);
    }

    /**
     * Atualiza compra e retorna o valor total do pedido.
     *
     * @return TotalPriceDTO
     * @author Juliano Alcione de Souza
     */

    @PutMapping
    public ResponseEntity<TotalPriceDTO> putOrder(@RequestBody PurchaseOrderForm form) {
        PurchaseOrder order = form.convertToEntity(tokenService);
        PurchaseOrder savedOrder = service.updateCartList(order);
        BigDecimal totalPrice = service.calculateTotalPrice(savedOrder);

        return new ResponseEntity(new TotalPriceDTO(totalPrice), HttpStatus.OK);
    }


    /**
     * Este endpoint retorna todos os produtos de um pedido.
     * @return  OrderProductDTO
     * @author Antonio Hugo
     * Refactor: Ana Preis
     */

    @GetMapping("")
    public ResponseEntity<OrderProductDTO> getProductsByUser() {
            User userLogged = tokenService.getUserLogged();
            System.out.println(userLogged.getId());
            PurchaseOrder purchaseOrder = service.findPurchaseByUser(userLogged);
            List<Product> products = service.findProductsByOrderId(purchaseOrder.getId());
        return ResponseEntity.ok().body(new OrderProductDTO(purchaseOrder.getId(), products));

    }

    /**
     * Este endpoint atualiza o OrderStatus do pedido de compra para FINISHED.
     * @param idOrder
     * @return  TotalPriceDTO
     * @author Ana Preis
     */
    @PutMapping("/finish")
    public ResponseEntity<TotalPriceDTO> updateOrderStatus(@RequestParam Integer idOrder) {
        PurchaseOrder purchaseOrder = service.updateOrderStatus(idOrder);
        BigDecimal totalPrice = service.calculateTotalPrice(purchaseOrder);
        return ResponseEntity.ok().body(new TotalPriceDTO(totalPrice));
    }
}
