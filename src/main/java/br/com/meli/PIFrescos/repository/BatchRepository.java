package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.controller.dtos.WarehouseQuantityDTO;
import br.com.meli.PIFrescos.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query(nativeQuery = true, value = "select w.warehouse_code as warehousecode, sum(b.current_quantity) as totalquantity from batch as b join inbound_order as io on io.order_number = b.inbound_order_order_number join products as p on p.product_id = b.product_product_id join section as s on s.section_code = io.section_section_code join warehouse as w on w.warehouse_code = s.warehouse_warehouse_code where b.product_product_id = 1 group by w.warehouse_code, p.product_name")
    List<WarehouseQuantityDTO> getQuantityProductByWarehouse(@Param("productId") Integer productId);
}
