package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
