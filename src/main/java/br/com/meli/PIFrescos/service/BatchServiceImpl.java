package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.dtos.batch.BatchFromDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BatchServiceImpl implements IBatchService {

    private final BatchRepository batchRepository;
    private final ProductService productService;

    /**
     *
     * @param inboundOrder
     * @param batchFromDTO
     * @return retorna a Batch criada
     * @author Antonio Hugo
     */
    @Override
    public Batch createBatch(InboundOrder inboundOrder, BatchFromDTO batchFromDTO) {

        Product productExist = productService.findProductById(batchFromDTO.getProductId());

        Batch batch = Batch.builder()
                .batchNumber(batchFromDTO.getBatchNumber())
                .currentTemperature(batchFromDTO.getCurrentTemperature())
                .minimumTemperature(batchFromDTO.getMinimumTemperature())
                .initialQuantity(batchFromDTO.getInitialQuantity())
                .currentQuantity(batchFromDTO.getCurrentQuantity())
                .manufacturingDate(batchFromDTO.getManufacturingDate())
                .expiringDate(batchFromDTO.getExpiringDate())
                .dueDate(batchFromDTO.getDueDate())
                .inboundOrder(inboundOrder)
                .product(productExist)
                .build();

        return batchRepository.save(batch);
    }

    @Override
    public Batch updateBatch(Integer batchNumber, Section section) {
        return null;
    }
}
