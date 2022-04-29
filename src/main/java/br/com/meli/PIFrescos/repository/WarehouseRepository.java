package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Marcelo Leite
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
