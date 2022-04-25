package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.InboundOrder;

public interface IInboundOrderService {

    InboundOrder save(InboundOrder inboundOrder);

    InboundOrder update(Integer inboundOrderNumber, InboundOrder newInboundOrderValues);

    void delete(Integer inboundOrderId);
}
