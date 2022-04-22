package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.Section;

/**
 * @author Ana Preis
 */
public interface ISectionService {

    Section createSection(Section section);
    Integer updateCapacity(Integer sectionCode, Integer quantity);
}
