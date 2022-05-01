package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InboundOrderRepository inboundOrderRepository;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private BatchServiceImpl batchService;

    @Mock
    private SectionService sectionService;

    @InjectMocks
    private InboundOrderService inboundOrderService;

    private Warehouse warehouse = new Warehouse();
    private Section section = new Section();
    private InboundOrder inboundOrder = new InboundOrder();
    private Batch batch1 = new Batch();
    private Batch batch2 = new Batch();
    private Product product1 = new Product();
    private Product product2 = new Product();

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse(1, new ArrayList<>());
        section = new Section(1, StorageType.FRESH, 10, 0, warehouse);
        product1 = new Product(1,"product 1",StorageType.FRESH,"brief description");
        product2 = new Product(2,"product 2",StorageType.REFRIGERATED,"brief description");
        batch1.setCurrentQuantity(2);
        batch2.setCurrentQuantity(3);
        batch1.setProduct(product1);
        batch2.setProduct(product2);

        inboundOrder = new InboundOrder(1, LocalDate.now(), section, Arrays.asList(batch1, batch2));
    }

    @Test
    public void saveNewInboundOrder() {
        Integer sectionCode = section.getSectionCode();
        Integer quantity = inboundOrder.getBatchStock().stream().mapToInt(batch -> batch.getCurrentQuantity()).sum();

        Mockito.when(productRepository.findById(null)).thenReturn(java.util.Optional.ofNullable(product1));
        Mockito.when(sectionService.updateCapacity(sectionCode, quantity)).thenReturn(5);
        Mockito.when(inboundOrderRepository.save(inboundOrder)).thenReturn(inboundOrder);

        InboundOrder savedInboundOrder = inboundOrderService.save(inboundOrder);

        assertEquals(inboundOrder, savedInboundOrder);
    }

    @Test
    public void updateExistingInboundOrder() {
        Batch batch3 = new Batch();
        batch3.setCurrentQuantity(3);
        batch3.setProduct(new Product());
        batch3.setBatchNumber(3);
        Integer id = inboundOrder.getOrderNumber();

        InboundOrder newInboundOrderValues = new InboundOrder();
        Section newSection = new Section(1, StorageType.FRESH, 10, 0, warehouse);
        newInboundOrderValues.setSection(newSection);
        newInboundOrderValues.setBatchStock(Arrays.asList(batch1,batch2,batch3));

        InboundOrder expectedInboundOrder = inboundOrder;
        expectedInboundOrder.setSection(newSection);

        Mockito.when(productRepository.findById(product1.getProductId())).thenReturn(java.util.Optional.ofNullable(product1));
        Mockito.when(productRepository.findById(product2.getProductId())).thenReturn(java.util.Optional.ofNullable(product2));
        Mockito.when(inboundOrderRepository.getByOrderNumber(id)).thenReturn(inboundOrder);
        Mockito.when(inboundOrderRepository.save(any())).thenReturn(inboundOrder);
        //Mockito.when(batchRepository.existsBatchByBatchNumber(batch1.getBatchNumber())).thenReturn(true);

        InboundOrder updatedInboundOrder = inboundOrderService.update(id, newInboundOrderValues);

        assertEquals(expectedInboundOrder, updatedInboundOrder);
        assertEquals(expectedInboundOrder.getBatchStock().size(), updatedInboundOrder.getBatchStock().size());
    }

    @Test
    public void deleteExistingInboundOrder() {
        Integer id = inboundOrder.getOrderNumber();
        Integer sectionCode = section.getSectionCode();
        Integer quantity = inboundOrder.getBatchStock().stream().mapToInt(batch -> batch.getCurrentQuantity()).sum();

        Mockito.when(inboundOrderRepository.getById(id)).thenReturn(inboundOrder);
        Mockito.when(sectionService.updateCapacity(sectionCode, -quantity)).thenReturn(0);

        inboundOrderService.delete(id);
        verify(inboundOrderRepository).deleteById(any());
    }

    @Test
    public void updateFailsWhenNotFoundId() {
        Integer notSavedId = 111;
        String errorMessage = "Ordem solicitada não existe.";

        Mockito.when(inboundOrderRepository.getByOrderNumber(notSavedId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboundOrderService.update(notSavedId, inboundOrder));

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void postFailsWhenSectionAndProductTypeMismatches(){

        String errorMessage = "Tipo de produto e local de armazenamento incompatíveis.";


        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(product1));
        Mockito.when(productRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(product2));

        Mockito.when(sectionService.findById(1)).thenReturn(java.util.Optional.ofNullable(section));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboundOrderService.save(inboundOrder));

        assertEquals(errorMessage, exception.getMessage());

    }
}