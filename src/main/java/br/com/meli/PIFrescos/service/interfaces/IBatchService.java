package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.Batch;

import java.util.List;


public interface IBatchService {

    Batch findBatchById(Integer id);
    List<Batch> findBatchesByProduct(Integer productId);
    boolean checkIfBatchExists(Batch batch);
}
