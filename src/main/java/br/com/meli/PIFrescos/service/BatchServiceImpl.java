package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;

import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BatchServiceImpl implements IBatchService {

    private final BatchRepository batchRepository;

    /**
     *
     * @return retorna uma Batch
     * @author Antonio Hugo
     */
    @Override
    public Batch findBatchById(Integer id) {
        return batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    /**
     *
     * @return retorna uma Lista Batch
     * @author Antonio Hugo
     */
    @Override
    public List<Batch> findBatchesByProduct(Integer productId) {
        return this.batchRepository.findBatchesByProduct_ProductId(productId);
    }

    /**
     * @param batch
     * @return boolean
     */
    @Override
    public boolean checkIfBatchExists(Batch batch) {
        return batchRepository.existsBatchByBatchNumber(batch.getBatchNumber());
    }

}
