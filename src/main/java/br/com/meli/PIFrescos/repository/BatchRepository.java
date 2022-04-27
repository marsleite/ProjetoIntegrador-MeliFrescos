package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * @author Ana Preis
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findBatchesByProduct_ProductId(Integer productId);

    boolean existsBatchByBatchNumber(Integer batchNumber);
    Batch findByBatchNumber(Integer batchNumber);
}
