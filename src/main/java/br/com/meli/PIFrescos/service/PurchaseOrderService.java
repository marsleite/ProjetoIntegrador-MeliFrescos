package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.config.handler.ProductCartException;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;

import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ana Preis
 */
@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BatchRepository batchRepository;

    /**
     * Salva uma PurchaseOrder. Antes de salvar, é verificado se a PurchaseOrder já existe.
     * @return PurchaseOrder
     * @author Ana Preis
     */
    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        Optional<PurchaseOrder> purchaseOptional = purchaseOrderRepository.findById(purchaseOrder.getId());
        if (purchaseOptional.isPresent()) {
            throw new RuntimeException("PurchaseOrder already exists!");
        }

        List<ProductsCart> invalidProductList = purchaseOrder.getCartList().stream()
                .filter(productsCart -> batchRepository.existsBatchByBatchNumber(productsCart.getBatch().getBatchNumber()))
                .filter(productsCart -> !isProducstCartListQuantityValid(productsCart))
                .collect(Collectors.toList());

        if(!invalidProductList.isEmpty()){
            ProductCartException exception = new ProductCartException();
            invalidProductList.forEach(item -> {
                exception.addError(item.getBatch().getProduct().getProductName(), "Insuficient quantity of product on batch");
            });
            throw exception;
        }

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public boolean isProducstCartListQuantityValid(ProductsCart productsCart){
        Integer productCartQuantity = productsCart.getQuantity();
        Integer batchCurrentQuantity = batchRepository.findByBatchNumber(productsCart.getBatch().getBatchNumber()).getCurrentQuantity();

        if (batchCurrentQuantity.compareTo(productCartQuantity) < 0) { return false; }
        return true;
    }

    /**
     * Busca todas as PurchaseOrders. Antes de buscar, é verificado se a lista está vazia.
     * @return List<PurchaseOrder>
     * @author Ana Preis
     */
    @Override
    public List<PurchaseOrder> getAll(){
        List<PurchaseOrder> purchaseList = purchaseOrderRepository.findAll();
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
     * PurchaseOrder passada, deletando a antiga e salvando a nova.
     * @return void
     * @author Ana Preis
     */
    @Override
    public PurchaseOrder updateOrderStatus(OrderStatus newOrderStatus, Integer id){
        Optional<PurchaseOrder> purchaseOptional = purchaseOrderRepository.findById(id);
        if(purchaseOptional.isEmpty()){
            throw new EntityNotFoundException("PurchaseOrder not found");
        }

        PurchaseOrder purchase = purchaseOptional.get();
        purchase.setOrderStatus(newOrderStatus);
        purchaseOrderRepository.deleteById(id);

        return purchaseOrderRepository.save(purchase);
    }

    /**
     * Calcula o valor total dos produtos no carrinho de uma compra
     * @return BigDecimal
     * @author Julio César Gama
     * @author Felipe Myose
     * @author Antonio Hugo
     */
    public BigDecimal calculateTotalPrice(PurchaseOrder order){

        double totalPrice = order.getCartList()
                .stream().mapToDouble(productsCart ->  productsCart.getBatch().getUnitPrice().doubleValue() * productsCart.getQuantity()
          ).sum();

        return BigDecimal.valueOf(totalPrice);
    }

    /**
     * Busca todos os produtos do pedido.
     * @param orderId id do pedido
     * @return  List<Product>  lista de produtos do pedido
     * @author Antonio Hugo
     */
    public List<Product> findProductsByOrderId(Integer orderId) {

        return this.getById(orderId).getCartList().stream().map(pCart -> pCart.getBatch().getProduct()).collect(Collectors.toList());
    }
}
