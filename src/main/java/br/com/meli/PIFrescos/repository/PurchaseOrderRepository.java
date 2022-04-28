package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.PurchaseOrder;
import br.com.meli.PIFrescos.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Ana Preis
 */
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    PurchaseOrder findByUser(User user);

    Optional<PurchaseOrder> findByUserId(Integer id);

    @Query(value = "select p from PurchaseOrder p where p.orderStatus = 'OPENED' and p.user.id = :userId")
    PurchaseOrder getPurchaseOrdersByUserIdAndOrderStatusIsOPENED(@Param("userId") Integer userId);

}

