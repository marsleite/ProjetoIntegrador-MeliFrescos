package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.InboundOrder;

import java.util.List;

public interface IInboundOrderService {

    List<InboundOrder> getAll();

    InboundOrder save(InboundOrder inboundOrder);

    InboundOrder update(Integer inboundOrderNumber, InboundOrder newInboundOrderValues);

    void delete(Integer inboundOrderId);
}
