package br.com.meli.PIFrescos.service.interfaces;


import br.com.meli.PIFrescos.models.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ana Preis
 */
public interface IPurchaseOrderService {
    PurchaseOrder save(PurchaseOrder purchaseOrder);
    boolean isProductsCartListQuantityValid(ProductsCart productsCart);
    List<PurchaseOrder> getAllByUserId(Integer id);
    PurchaseOrder getById(Integer id);
    void delete(Integer id);
    PurchaseOrder updateOrderStatus(Integer id);
    BigDecimal calculateTotalPrice(PurchaseOrder order);
    List<Product> findProductsByOrderId(Integer orderId);
    PurchaseOrder updateCartList(PurchaseOrder newPurchaseOrder);
    PurchaseOrder findPurchaseByUser(User user);
    PurchaseOrder getPurchaseOrderByUserIdAndStatusIsOpened(Integer userId);
}
