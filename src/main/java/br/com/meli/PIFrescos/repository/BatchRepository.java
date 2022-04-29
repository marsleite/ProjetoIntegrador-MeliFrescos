package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "select b from Batch b inner join Product p on p = b.product where p.productId = :productId order by b.batchNumber")
    List<Batch> findBatchesByProduct_ProductIdAndOrderByBatchNumber(@Param("productId") Integer productId);

    @Query(value = "select b from Batch b inner join Product p on p = b.product where p.productId = :productId order by b.currentQuantity")
    List<Batch> findBatchesByProduct_ProductIdAndOrderByCurrentQuantity(@Param("productId") Integer productId);

    @Query(value = "select b from Batch b inner join Product p on p = b.product where p.productId = :productId order by b.dueDate")
    List<Batch> findBatchesByProduct_ProductIdAndOrderBOrderByDueDate(@Param("productId") Integer productId);
}
