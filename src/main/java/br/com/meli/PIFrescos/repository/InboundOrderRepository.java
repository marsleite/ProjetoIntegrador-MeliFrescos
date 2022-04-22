package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.InboundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Hugo
 */
@Repository
public interface InboundOrderRepository extends JpaRepository<InboundOrder, Integer> {
}
