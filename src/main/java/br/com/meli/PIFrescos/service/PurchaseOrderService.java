package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.config.handler.ProductCartException;

import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Ana Preis
 */
@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    IBatchService batchService;

    /**
     * Salva uma PurchaseOrder. Antes de salvar, é necessário buscar informaçoes das entidades que o compõe.
     * @return PurchaseOrder
     * @author Ana Preis / Felipe Myose
     */
    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
         // se o usuario já possui uma ordem de compra, ao fazer GET, os itens existentes serão substituidos
        PurchaseOrder oldPurchaseOrder = null;
        try {
            oldPurchaseOrder = getPurchaseOrderByUserIdAndStatusIsOpened(purchaseOrder.getUser().getId());
            purchaseOrder.setId(oldPurchaseOrder.getId());
        } catch (EntityNotFoundException e) {
            System.out.println("Creating new purchaseorder");
        }
        //encontrar o batch
        purchaseOrder.getCartList().forEach(productsCart -> {
            Integer batchNumber = productsCart.getBatch().getBatchNumber();
            Batch batch = batchService.findByBatchNumber(batchNumber);
            productsCart.setBatch(batch);
        });
        validProductList(purchaseOrder);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public void validProductList(PurchaseOrder purchaseOrder) {
        List<ProductsCart> invalidProductList = purchaseOrder.getCartList().stream()
                .filter(productsCart -> !isProductsCartListQuantityValid(productsCart))
                .collect(Collectors.toList());

        if (!invalidProductList.isEmpty())
            throw new ProductCartException(invalidProductList);
    }

    /**
     * Verifica se a quantidade atual de produtos no Lote é maior do que a quantidade do pedido.
     * @return boolean
     * @author Ana Preis
     */
    @Override
    public boolean isProductsCartListQuantityValid(ProductsCart productsCart){
        Integer productCartQuantity = productsCart.getQuantity();
        Batch batch = batchService.findByBatchNumber(productsCart.getBatch().getBatchNumber());
        Integer batchCurrentQuantity = batch.getCurrentQuantity();

        boolean isValid = batchCurrentQuantity.compareTo(productCartQuantity) > 0;
        if(!isValid)
            productsCart.setBatch(batch);

        return isValid;
    }

    /**
     * Busca todas as PurchaseOrders. Antes de buscar, é verificado se a lista está vazia.
     * @return List<PurchaseOrder>
     * @author Ana Preis
     */
    @Override
    public List<PurchaseOrder> getAllByUserId(Integer id){
        List<PurchaseOrder> purchaseList = purchaseOrderRepository.findAllByUserId(id);
        if(purchaseList.isEmpty()){
            throw new EntityNotFoundException("PurchaseOrder list is empty");
        }
        return purchaseList;
    }

    /**
     * Busca todas a PurchaseOrder pela id. Antes de buscar, é verificado se existe.
     * @return PurchaseOrder
     * @author Ana Preis
     */
    @Override
    public PurchaseOrder getById(Integer id){
        Optional<PurchaseOrder> purchaseOptional = purchaseOrderRepository.findById(id);
        if(purchaseOptional.isEmpty()){
            throw new EntityNotFoundException("PurchaseOrder not found");
        }
        return purchaseOptional.get();
    }

    /**
     * Verifica se a PurchaseOrder existe e se sim, deleta.
     * @return void
     * @author Ana Preis
     */
    @Override
    public void delete(Integer id){
        Optional<PurchaseOrder> purchaseOptional = purchaseOrderRepository.findById(id);
        if(purchaseOptional.isEmpty()){
            throw new EntityNotFoundException("PurchaseOrder not found");
        }
        purchaseOrderRepository.deleteById(id);
    }

    /**
     * Verifica se a PurchaseOrder existe e se a OrderStatus existe. Se sim, atualiza a OrderStatus da
     * PurchaseOrder passada, atualizando a quantidade no Batch e salvando a nova.
     * @return void
     * @author Ana Preis
     */
    @Transactional
    @Override
    public PurchaseOrder updateOrderStatus(Integer id){
        Optional<PurchaseOrder> purchaseOptional = purchaseOrderRepository.findById(id);
        if(purchaseOptional.isEmpty()){
            throw new EntityNotFoundException("PurchaseOrder not found");
        }
        PurchaseOrder purchase = purchaseOptional.get();

        if(purchase.getOrderStatus().equals(OrderStatus.OPENED)){
            purchase.setOrderStatus(OrderStatus.FINISHED);
            purchase.getCartList().forEach(productsCart -> {
                batchService.updateCurrentQuantity(productsCart.getQuantity(), productsCart.getBatch());
            });
        }
        return purchase;
    }

    /**
     * Calcula o valor total dos produtos no carrinho de uma compra
     * @return BigDecimal
     * @author Julio César Gama
     * @author Felipe Myose
     * @author Antonio Hugo
     */
    @Override
    public BigDecimal calculateTotalPrice(PurchaseOrder order){
        double totalPrice = order.getCartList()
                .stream().mapToDouble(productsCart ->  productsCart.getBatch().getUnitPrice().doubleValue() * productsCart.getQuantity()
          ).sum();

        return BigDecimal.valueOf(totalPrice);
    }

    /**
     * Busca todos os produtos do pedido.
     * @param orderId id do pedido
     * @return  lista de produtos do pedido
     * @author Antonio Hugo
     */
    @Override
    public List<Product> findProductsByOrderId(Integer orderId) {

        return this.getById(orderId).getCartList().stream().map(pCart -> pCart.getBatch().getProduct()).collect(Collectors.toList());
    }

    /**
     * Atualiza todos os itens do carrinho
     * @param newPurchaseOrder
     * @return  PurchaseOrder
     * @author Juliano Alcione de Souza
     */
    @Transactional
    @Override
    public PurchaseOrder updateCartList(PurchaseOrder newPurchaseOrder) {
        validProductList(newPurchaseOrder);
        return purchaseOrderRepository.save(newPurchaseOrder);
    }

    @Override
    public PurchaseOrder findPurchaseByUser(User user){
      return purchaseOrderRepository.findByUser(user);
    }

    /**
     * Verifica se um usuario ja possui uma order/carrinho de compra.
     * @param userId
     * @return PurchaseOrder
     */
    @Override
    public PurchaseOrder getPurchaseOrderByUserIdAndStatusIsOpened(Integer userId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.getPurchaseOpenedByUserId(userId);
        if (purchaseOrders.size() > 0) {
            return purchaseOrders.get(0);
        }
        throw new EntityNotFoundException("No OPENED purchase cart");
    }
}
