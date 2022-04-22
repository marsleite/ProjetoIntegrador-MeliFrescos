package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * @author Ana Preis
 */

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    public Integer updateCapacity(Integer sectionCode, Integer newCapacity){
        Section section = sectionRepository.findById(sectionCode).get();
        if (section == null){
            throw new RuntimeException("A section não existe");
        }

        if (section.getCurrentCapacity() + newCapacity < section.getMaxCapacity()) {
            section.setCurrentCapacity(section.getCurrentCapacity() + newCapacity);
            return section.getCurrentCapacity();
        }
        throw new RuntimeException("A capacidade do setor não suporta essa order");
    }
}
