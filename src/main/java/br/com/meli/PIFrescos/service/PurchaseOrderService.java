package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.OrderStatus;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


/**
 * @author Ana Preis
 */
@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

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
        return purchaseOrderRepository.save(purchaseOrder);
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
}
