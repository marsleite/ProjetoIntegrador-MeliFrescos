package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.service.interfaces.IInboundOrderService;
import br.com.meli.PIFrescos.service.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundOrderService implements IInboundOrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InboundOrderRepository inboundOrderRepository;

    @Autowired
    ISectionService sectionService;


    /**
     * Salva uma inbound order. Antes de salvar, é verificado se a Section correspondente ainda possui capacidade
     * para armazenar.
     * @return List<inboundOrder>
     * @author Julio César Gama
     */
    @Override
    public List<InboundOrder> getAll() {
        return inboundOrderRepository.findAll();
    }

    /**
     * Salva uma inbound order. Antes de salvar, é verificado se a Section correspondente ainda possui capacidade
     * para armazenar, se o produto está cadastrado.
     * @param inboundOrder
     * @return inboundOrder
     * @author Felipe Myose
     */
    @Override
    public InboundOrder save(InboundOrder inboundOrder) {

        // check if product from batch exists
        // then update the batch details
        inboundOrder.getBatchStock().stream().forEach(batch -> {
            Product product = productRepository.findById(batch.getProduct().getProductId()).orElse(null);
            if (product == null) {
                throw new RuntimeException("Produto não existe.");
            }
            batch.setProduct(product);
        });

        // verificar a capacidade e atualizá-lo
        sectionService.updateCapacity(inboundOrder.getSection().getSectionCode(),
                calculateVolume(inboundOrder));

        // salvar previamene para ter o id do inbound order - este valor deve entrar na lista de batch
        InboundOrder savedInboundOrder = inboundOrderRepository.save(inboundOrder);
        savedInboundOrder.getBatchStock().stream().forEach(batch -> {
            batch.setInboundOrder(savedInboundOrder);
        });

        return inboundOrderRepository.save(savedInboundOrder);
    }

    /**
     * Atualiza uma entrada existente
     * @param inboundOrderNumber
     * @param newInboundOrderValues
     * @return inboundOrder
     */
    @Override
    public InboundOrder update(Integer inboundOrderNumber, InboundOrder newInboundOrderValues) {
        InboundOrder inboundOrder = inboundOrderRepository.getById(inboundOrderNumber);

        if(inboundOrder == null) {
            throw new RuntimeException("Ordem solicitada não existe.");
        }

        inboundOrder.setOrderDate(newInboundOrderValues.getOrderDate());
        inboundOrder.setOrderDate(newInboundOrderValues.getOrderDate());
        inboundOrder.setSection(newInboundOrderValues.getSection());
        inboundOrder.setBatchStock(newInboundOrderValues.getBatchStock());

        return inboundOrderRepository.save(inboundOrder);
    }

    /**
     * Remove um inbound order. Quando removido, atualizar a capacidade do setor correspondente -
     * subtraindo a quantidade correspondente.
     * @param inboundOrderId
     * @author Felipe Myose
     */
    @Override
    public void delete(Integer inboundOrderId) {
        InboundOrder inboundOrder = inboundOrderRepository.getById(inboundOrderId);

        inboundOrderRepository.deleteById(inboundOrderId);
        sectionService.updateCapacity(inboundOrder.getSection().getSectionCode(),
                -calculateVolume(inboundOrder));
    }

    /**
     * Calcula o volume do inbound order.
     * @param inboundOrder
     * @return integer
     * @author Felipe Myose
     */
    private Integer calculateVolume(InboundOrder inboundOrder) {
        return inboundOrder.getBatchStock().stream().mapToInt(batch -> batch.getCurrentQuantity()).sum();
    }
}
