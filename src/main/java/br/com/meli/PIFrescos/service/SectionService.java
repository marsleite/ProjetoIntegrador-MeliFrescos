package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Ana Preis
 */

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    /**
     * @author Ana Preis
     * Cria uma nova section do warehouse
     */
    public Section createSection(Section section) {
        return sectionRepository.save(section);
    }

    /**
     * @author Ana Preis
     * Recebe a id de uma section e a quantidade a ser adicionada ao currentCapacity
     * Verifica se a section existe e se, ao adicionar a quantidade nova, não ultrapassa o maxCapacity.
     * Se estiver tudo certo, atualiza o currentCapacity.
     */
    public Integer updateCapacity(Integer sectionCode, Integer quantity){
        Section section = sectionRepository.findById(sectionCode).get();
        if (section == null){
            throw new RuntimeException("A section não existe");
        }

        if (section.getCurrentCapacity() + quantity < section.getMaxCapacity()) {
            section.setCurrentCapacity(section.getCurrentCapacity() + quantity);
            return section.getCurrentCapacity();
        }
        throw new RuntimeException("A capacidade do setor não suporta essa order");
    }
}
