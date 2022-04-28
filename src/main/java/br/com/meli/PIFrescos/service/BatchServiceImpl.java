package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;

import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    private BatchRepository batchRepository;

    /**
     * @param id batch id
     * @return Batch
     * @author Antonio Hugo
     */
    @Override
    public Batch findBatchById(Integer id) {
        return batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    /**
     * @param productId  id do produto
     * @return List<Batch>
     * @author Antonio Hugo
     */
    @Override
    public List<Batch> findBatchesByProduct(Integer productId) {
        return this.batchRepository.findBatchesByProduct_ProductId(productId);
    }

    /**
     * @param batch recebe um Batch
     * @return boolean
     */
    @Override
    public boolean checkIfBatchExists(Batch batch) {
        return batchRepository.existsBatchByBatchNumber(batch.getBatchNumber());
    }

}
