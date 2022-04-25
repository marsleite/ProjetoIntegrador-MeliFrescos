package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IInboundOrderService;
import br.com.meli.PIFrescos.service.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InboundOrderService implements IInboundOrderService {

    @Autowired
    InboundOrderRepository inboundOrderRepository;

    @Autowired
    ISectionService sectionService;

    /**
     * Salva uma inbound order. Antes de salvar, é verificado se a Section correspondente ainda possui capacidade
     * para armazenar.
     * @param inboundOrder
     * @return inboundOrder
     * @author Felipe Myose
     */
    @Override
    public InboundOrder save(InboundOrder inboundOrder) {
        sectionService.updateCapacity(inboundOrder.getSection().getSectionCode(),
                calculateVolume(inboundOrder));

        return inboundOrderRepository.save(inboundOrder);
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
