package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PurchaseOrderForm {

    @NotNull(message = "User field can't be empty")
    @ManyToOne
    private Integer userId;
    private LocalDate date;
    @NotNull(message = "OrderStatus field can't be empty")
    private OrderStatus orderStatus;
    @NotNull(message = "ProductsCart list field can't be empty")
    private List<ProductCartForm> cartList;

    public static PurchaseOrder convertToEntity(PurchaseOrderForm purchaseOrderForm){
        User user = new User();
        user.setId(purchaseOrderForm.getUserId());
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUser(user);
        purchaseOrder.setOrderStatus(OrderStatus.OPENED);
        purchaseOrder.setDate(LocalDate.now());
        purchaseOrder.setCartList(purchaseOrderForm.getCartList()
                .stream().map(productsCartForm -> ProductCartForm.convert(productsCartForm))
                .collect(Collectors.toList()));

        return  purchaseOrder;
    }
}
