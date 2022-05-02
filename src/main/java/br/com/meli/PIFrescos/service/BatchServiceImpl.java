package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;

import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public List<Batch> findBatchesByDueDateGreaterThanEqualAndSectorEquals(Integer expiringLimit, Integer sectionId) {
        LocalDate maxDueDate = LocalDate.now().plusDays(expiringLimit);
        List<Batch> batches = batchRepository.findBatchesByDueDateGreaterThanEqualAndSectorEquals(maxDueDate, sectionId);

        return batches;
    }

    /**
     * Atualiza o a CurrentQuantity do Batch de acordo com a quantidade do ProductCart enviada por parametro.
     * @param quantity, batch
     * @return  Batch
     * @author Ana Preis
     */
    @Transactional
    @Override
    public Batch updateCurrentQuantity(Integer quantity, Batch batch){
        Optional<Batch> batchOptional = batchRepository.findById(batch.getBatchNumber());
        if (batchOptional.isEmpty()){
            throw new EntityNotFoundException("Can`t update quantity, Batch not found");
        }
        Batch newBatch = batchOptional.get();
        newBatch.setCurrentQuantity(newBatch.getCurrentQuantity() - quantity);
        return newBatch;
    }

    /**
     * Procura a lista de batches ordenada por Lote (L), CurrentQuantity(C) ou DueDate(F).
     * Se não for passado na query nenhum destes 3, retorna uma exceção.
     * @author Ana Preis
     */
    public List<Batch> findBatchesByProductOrderBy(Integer id, String orderBy){
        if(orderBy.equals("")){
            return batchRepository.findBatchesByProduct_ProductId(id);
        }
        if(orderBy.equalsIgnoreCase("L")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderByBatchNumber(id);
        }
        if(orderBy.equalsIgnoreCase("C")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderByCurrentQuantity(id);
        }
        if(orderBy.equalsIgnoreCase("F")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderBOrderByDueDate(id);
        }
        else {
            throw new RuntimeException("Invalid query for OrderBy");
        }
    }

}
