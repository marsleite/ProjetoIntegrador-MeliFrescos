package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ana Preis
 */
@ExtendWith(MockitoExtension.class)
public class SectionServiceTests {

    @Mock
    SectionRepository sectionRepository;

    @InjectMocks
    SectionService sectionService;

    Warehouse warehouse = new Warehouse();
    Section section = new Section();

    @BeforeEach
    void setup() {
        warehouse = new Warehouse(1, new ArrayList<>());
        section = new Section(1, StorageType.FRESH, 10, 0, warehouse);
    }

    @Test
    void saveNewSection(){
        Mockito.when(sectionRepository.save(section)).thenReturn(section);
        Mockito.when(sectionService.sectionAlreadyExists(section.getSectionCode())).thenReturn(false);

        Section savedSection = sectionService.createSection(section);

        assertEquals(section, savedSection);
    }

    @Test
    void shouldUpdateSection() {
        Mockito.when(sectionRepository.save(section)).thenReturn(section);
        Mockito.when(sectionService.sectionAlreadyExists(section.getSectionCode())).thenReturn(false);

        Section savedSection = sectionService.createSection(section);

        Mockito.when(sectionRepository.findById(1)).thenReturn(Optional.ofNullable(savedSection));

        Integer newCapacity = sectionService.updateCapacity(1, 5);

        assertEquals(5, newCapacity);
    }
}
