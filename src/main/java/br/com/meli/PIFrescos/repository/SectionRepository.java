package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Antonio Hugo
 */
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    Boolean existsSectionBySectionCode(Integer code);
}
