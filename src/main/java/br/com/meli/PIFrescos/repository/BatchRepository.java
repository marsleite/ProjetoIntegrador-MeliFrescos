package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ana Preis
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
}
