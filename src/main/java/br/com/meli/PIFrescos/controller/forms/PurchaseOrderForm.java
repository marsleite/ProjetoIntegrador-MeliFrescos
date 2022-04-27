package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class PurchaseOrderForm {

    @NotNull(message = "User field can't be empty")
    @ManyToOne
    private User user;
    private LocalDate date;
    @NotNull(message = "OrderStatus field can't be empty")
    private OrderStatus orderStatus;
    @NotNull(message = "ProductsCart list field can't be empty")
    private List<ProductsCart> cartList;

    public PurchaseOrder convertToEntity(){
        return new PurchaseOrder(null, user, date, orderStatus, cartList);
    }
}
