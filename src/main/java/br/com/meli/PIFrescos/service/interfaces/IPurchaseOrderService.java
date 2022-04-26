package br.com.meli.PIFrescos.service.interfaces;


import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.PurchaseOrder;

import java.util.List;

/**
 * @author Ana Preis
 */
public interface IPurchaseOrderService {
    PurchaseOrder save(PurchaseOrder purchaseOrder);
    List<PurchaseOrder> getAll();
    PurchaseOrder getById(Integer id);
    void delete(Integer id);
    PurchaseOrder updateOrderStatus(OrderStatus newOrderStatus, Integer id);
}
