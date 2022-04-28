package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Ana Preis
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    PurchaseOrder findByUser(User user);
}