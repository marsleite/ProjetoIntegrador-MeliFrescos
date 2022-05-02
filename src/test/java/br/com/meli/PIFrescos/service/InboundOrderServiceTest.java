package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
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


/**
 * @author Felipe Myose
 */
@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private InboundOrderRepository inboundOrderRepository;

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
    private Product product3 = new Product();

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse(1, new ArrayList<>());
        section = new Section(1, StorageType.FRESH, 10, 0, warehouse);
        product1 = new Product(1,"product 1",StorageType.FRESH,"brief description");
        product2 = new Product(2,"product 2",StorageType.FRESH,"brief description");
        product3 = new Product(2,"product 3",StorageType.REFRIGERATED,"brief description");
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

        Mockito.when(productService.findProductById(1)).thenReturn(product1);
        Mockito.when(productService.findProductById(2)).thenReturn(product2);
        Mockito.when(sectionService.findById(any())).thenReturn(java.util.Optional.ofNullable(section));
        Mockito.when(sectionService.updateCapacity(sectionCode, quantity)).thenReturn(5);
        Mockito.when(inboundOrderRepository.save(inboundOrder)).thenReturn(inboundOrder);

        InboundOrder savedInboundOrder = inboundOrderService.save(inboundOrder);

        assertEquals(inboundOrder, savedInboundOrder);
    }

    @Test
    public void updateExistingInboundOrder() {
        product3.setProductType(StorageType.FRESH);
        Batch batch3 = new Batch();
        batch3.setCurrentQuantity(3);
        batch3.setProduct(product3);
        batch3.setBatchNumber(3);
        Integer id = inboundOrder.getOrderNumber();

        InboundOrder newInboundOrderValues = new InboundOrder();
        Section newSection = new Section(2, StorageType.FRESH, 10, 0, warehouse);
        newInboundOrderValues.setSection(newSection);
        newInboundOrderValues.setBatchStock(Arrays.asList(batch1,batch2,batch3));

        InboundOrder expectedInboundOrder = inboundOrder;
        expectedInboundOrder.setSection(newSection);

        Mockito.when(productService.findProductById(product1.getProductId())).thenReturn(product1);
        Mockito.when(productService.findProductById(product2.getProductId())).thenReturn(product2);
        Mockito.when(productService.findProductById(product3.getProductId())).thenReturn(product3);
        Mockito.when(sectionService.findById(any())).thenReturn(java.util.Optional.ofNullable(section));
        Mockito.when(inboundOrderRepository.getByOrderNumber(id)).thenReturn(inboundOrder);
        Mockito.when(inboundOrderRepository.save(any())).thenReturn(inboundOrder);

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

    /**
     * @author Julio Gama
     */
    @Test
    public void postFailsWhenSectionAndProductTypeMismatches(){
        product2.setProductType(StorageType.FROZEN);
        String errorMessage = "Tipo de produto e local de armazenamento incompatíveis.";

        Mockito.when(productService.findProductById(1)).thenReturn(product1);
        Mockito.when(productService.findProductById(2)).thenReturn(product2);

        Mockito.when(sectionService.findById(1)).thenReturn(java.util.Optional.ofNullable(section));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboundOrderService.save(inboundOrder));

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void saveInboundOrderWhenBatchListIsEmpty() {
        inboundOrder.setBatchStock(new ArrayList<>());
        Mockito.when(inboundOrderRepository.save(inboundOrder)).thenReturn(inboundOrder);
        InboundOrder savedInboundOrder = inboundOrderService.save(inboundOrder);

        assertEquals(inboundOrder, savedInboundOrder);
    }

    @Test
    public void tryToSaveInboundOrderWithUnknownSection() {
        String exceptionMessage = "Seção não existente.";
        inboundOrder.getSection().setSectionCode(111);
        Mockito.when(productService.findProductById(1)).thenReturn(product1);
        Mockito.when(productService.findProductById(2)).thenReturn(product2);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboundOrderService.save(inboundOrder));

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void tryToUpdateInboundOrderWithUnknownBatch() {
        String exceptionMessage = "Batch não existe. Lista de Batch vazia.";
        inboundOrder.setBatchStock(new ArrayList<>());
        Integer inboundOrderId = inboundOrder.getOrderNumber();
        Mockito.when(inboundOrderRepository.getByOrderNumber(inboundOrderId)).thenReturn(inboundOrder);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> inboundOrderService.update(inboundOrderId, inboundOrder));
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void testFindAll() {
        List<InboundOrder> allInboundOrders = new ArrayList<>();
        allInboundOrders.add(inboundOrder);
        List<InboundOrder> expected = allInboundOrders;

        Mockito.when(inboundOrderRepository.findAll()).thenReturn(allInboundOrders);

        List<InboundOrder> inboundOrdersResult = inboundOrderService.getAll();

        assertEquals(expected.size(), inboundOrdersResult.size());
        assertEquals(expected.get(0), inboundOrdersResult.get(0));
    }
}