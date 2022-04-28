package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.PurchaseOrder;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Ana Preis
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    Optional<PurchaseOrder> findByUserId(Integer id);
}