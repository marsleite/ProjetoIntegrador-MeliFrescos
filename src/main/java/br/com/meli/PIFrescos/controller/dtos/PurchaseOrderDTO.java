package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO criado para exibir os as ordens de compra, de seu status,
 * com o produto, sua quantidade e o valor unitario.
 * @author Felipe Myose
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderDTO {

    private Integer purchaseOrderId;
    private OrderStatus orderStatus;
    private List<PurchaseCartItemDTO> items;

    public static PurchaseOrderDTO convert(PurchaseOrder purchaseOrder) {
        List<PurchaseCartItemDTO> purchaseCartItemDTOS = purchaseOrder.getCartList().stream()
                .map(item -> PurchaseCartItemDTO.convert(item))
                .collect(Collectors.toList());

        return new PurchaseOrderDTO().builder()
                .orderStatus(purchaseOrder.getOrderStatus())
                .purchaseOrderId(purchaseOrder.getId())
                .items(purchaseCartItemDTOS)
                .build();
    }

    public static List<PurchaseOrderDTO> convert(List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders.stream().map(purchaseOrder -> convert(purchaseOrder))
                .collect(Collectors.toList());
    }
}
