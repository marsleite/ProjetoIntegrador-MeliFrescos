package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.Batch;

import java.util.List;


public interface IBatchService {

    Batch findBatchById(Integer id);
    Batch findByBatchNumber(Integer id);
    List<Batch> findBatchesByProduct(Integer productId);
    boolean checkIfBatchExists(Batch batch);
    Batch updateCurrentQuantity(Integer quantity, Batch batch);
    List<Batch> findBatchesOrderBy(Integer days, String category, String order);
    List<Batch> findBatchesByDueDateGreaterThanEqualAndSectorEquals(Integer expiringLimit, Integer sectionId);
}
