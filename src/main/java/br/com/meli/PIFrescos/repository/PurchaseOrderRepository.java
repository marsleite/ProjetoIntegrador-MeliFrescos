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

    /**
     * Retorna todos os carrinhos abertos
     * @param id do usuario
     * @return  List<PurchaseOrder>
     * @author Juliano Alcione de Souza
     */
    @Query("select po from PurchaseOrder as po where po.user.id = :userId and po.orderStatus = 'OPENED'")
    List<PurchaseOrder> getPurchaseOpenedByUserId(@Param("userId") Integer userId);
}

