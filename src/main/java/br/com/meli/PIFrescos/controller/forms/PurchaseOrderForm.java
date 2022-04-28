package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.config.security.TokenService;
import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.ProductsCart;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PurchaseOrderForm {

    @NotNull(message = "ProductsCart list field can't be empty")
    private List<ProductCartForm> cartList;

    /**
     * ConvertePurchaseOrderForm para PurchaseOrder.
     * Diversos valores estarão nulos - serão tratados no serviço
     * @param purchaseOrderForm
     * @return purchseOrder
     * @author Felipe Myose
     */
    public PurchaseOrder convertToEntity(TokenService tokenService){
        User userLogged = tokenService.getUserLogged();
        List<ProductsCart> productsCart = cartList.stream().map(productsCartForm ->
                ProductCartForm.convert(productsCartForm)).collect(Collectors.toList());

        return new PurchaseOrder(userLogged, productsCart);
    }
}
