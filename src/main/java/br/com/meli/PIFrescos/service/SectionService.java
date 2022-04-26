package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.repository.SectionRepository;
import br.com.meli.PIFrescos.service.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author Ana Preis
 */

@Service
public class SectionService implements ISectionService {

    @Autowired
    SectionRepository sectionRepository;

    /**
     * @author Ana Preis
     * Verifica se a Section já existe pelo SectionCode e cria uma nova section do warehouse
     */
    public Section createSection(Section section) {
        if(sectionAlreadyExists(section.getSectionCode())){
            throw new RuntimeException("Section already exists!");
        }
        return sectionRepository.save(section);
    }

    /**
     * @author Ana Preis
     * Recebe a id de uma section e a quantidade a ser adicionada ao currentCapacity
     * Verifica se a section existe e se, ao adicionar a quantidade nova, não ultrapassa o maxCapacity.
     * Se estiver tudo certo, atualiza o currentCapacity.
     */
    public Integer updateCapacity(Integer sectionCode, Integer quantity){
        Optional<Section> sectionOptional = sectionRepository.findById(sectionCode);
        if (sectionOptional.isEmpty()){
            throw new RuntimeException("Invalid section");
        }
        Section section = sectionOptional.get();
        if (section.getCurrentCapacity() + quantity < section.getMaxCapacity()) {
            section.setCurrentCapacity(section.getCurrentCapacity() + quantity);
            return section.getCurrentCapacity();
        }
        throw new RuntimeException("Section max capacity does not support order");
    }
    public Boolean sectionAlreadyExists(Integer code){
        return sectionRepository.existsSectionBySectionCode(code);
    }
}
