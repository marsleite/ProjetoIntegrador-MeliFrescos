package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.SectionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ana Preis
 */
@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

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

    @Test
    void shouldNotCreateSection() {
        String message = "Section already exists!";
        Mockito.when(sectionService.sectionAlreadyExists(section.getSectionCode())).thenReturn(true);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> sectionService.createSection(section));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldNotFindSectionOnUpdate() {
        String message = "Invalid section";
        Mockito.when(sectionRepository.findById(section.getSectionCode())).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> sectionService.updateCapacity(1, 5));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void newCapacityIsGreaterThanMaxCapacity() {
        String message = "Section max capacity does not support order";
        Mockito.when(sectionRepository.save(section)).thenReturn(section);
        Mockito.when(sectionService.sectionAlreadyExists(section.getSectionCode())).thenReturn(false);

        Section savedSection = sectionService.createSection(section);

        Mockito.when(sectionRepository.findById(section.getSectionCode())).thenReturn(Optional.ofNullable(savedSection));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> sectionService.updateCapacity(1, 100));

        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
