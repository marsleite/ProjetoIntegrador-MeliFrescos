package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.dtos.batch.BatchFromDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Section;

public interface IBatchService {
    Batch createBatch(InboundOrder inboundOrder, BatchFromDTO batchFromDTO);
    Batch updateBatch(Integer batchNumber, Section section);
}
