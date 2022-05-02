package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.BatchCustomRepository;
import br.com.meli.PIFrescos.repository.BatchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Antonio Hugo
 *
 */
@ExtendWith(MockitoExtension.class)
public class BatchServiceTests {

    @InjectMocks
    private BatchServiceImpl batchService;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private BatchCustomRepository batchCustomRepository;

    Product mockProduct = new Product();
    Product mockProduct2 = new Product();
    Product mockProduct3 = new Product();
    InboundOrder mockInboundOrder = new InboundOrder();
    @BeforeEach
    public void setUp() {
        mockProduct.setProductId(1);
        mockProduct.setProductName("Test");
        mockProduct.setProductDescription("Product Test");
        mockProduct.setProductType(StorageType.FRESH);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera busar um Batch por Id.
     */
    @Test
    public void shouldBeAbleReturnBatchById(){

        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        Mockito.when(batchRepository.findById(ArgumentMatchers.any())).thenReturn(java.util.Optional.of(mockBatch));

        Batch result = batchService.findBatchById(1);

        Assertions.assertEquals(mockBatch, result);

    }

    /**
     * @author Antonio Hugo
     * Este teste espera uma RuntimeException quando o id do Batch for inválido.
     */
    @Test
    public void shouldNotBeAbleToFindBatchIfDoesntExist() {

        Assertions.assertThrows(RuntimeException.class, () -> batchService.findBatchById(100));

    }
    /**
     * @author Antonio Hugo
     * Este teste espera buscar pelo id do produto uma lista de Batch desse produto.
     */
    @Test
    public void shouldBeAbleReturnBatchListByProductId(){
        List listMockBatch = new ArrayList();

        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        listMockBatch.add(mockBatch);
        listMockBatch.add(mockBatch);

        Mockito.when(batchRepository.findBatchesByProduct_ProductId(ArgumentMatchers.any())).thenReturn(listMockBatch);

        List<Batch> result = batchService.findBatchesByProduct(1);

        Assertions.assertEquals(listMockBatch, result);

    }

    /**
     * @author Ana Preis
     * Espera atualizar a CurrentQuantity do Batch, recebendo a quantity e o batch a ser atuailzado
     * e retornando o Batch
     */
    @Test
    void shouldUpdateCurrentQuantity(){
        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        Mockito.when(batchRepository.findById(1)).thenReturn(Optional.of(mockBatch));

        Batch updatedBatch =  batchService.updateCurrentQuantity(4, mockBatch);

        Assertions.assertEquals(mockBatch, updatedBatch);
    }
    /**
     * @author Ana Preis
     * Espera mandar uma exception quando o Batch recebido não existir.
     */
    @Test
    void shouldNotFindBatchAndUpdateCurrentQuantity(){
        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        String message = "Can`t update quantity, Batch not found";
        Mockito.when(batchRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> batchService.updateCurrentQuantity(4, mockBatch));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * Deve retornar uma lista de lotes ordenados por Lote (L), CurrentQuantity(C) ou DueDate(F)
     * @author Ana Preis
     */
    @Test
    void shouldfindBatchesByProductIdAndOrderBy(){
        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.of(2022, 8, 10))
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        Batch mockBatch2 = Batch.builder()
                .batchNumber(2)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.of(2022, 8, 01))
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        List<Batch> list1 = Arrays.asList(mockBatch, mockBatch2);
        List<Batch> list2 = Arrays.asList(mockBatch2, mockBatch);

        Mockito.when(batchRepository.findBatchesByProduct_ProductIdAndOrderByBatchNumber(1)).thenReturn(list1);
        Mockito.when(batchRepository.findBatchesByProduct_ProductIdAndOrderByCurrentQuantity(1)).thenReturn(list1);
        Mockito.when(batchRepository.findBatchesByProduct_ProductIdAndOrderBOrderByDueDate(1)).thenReturn(list2);
        Mockito.when(batchRepository.findBatchesByProduct_ProductId(1)).thenReturn(list1);

        List<Batch> listByBatch = batchService.findBatchesByProductOrderBy(1, "L");
        List<Batch> listByQuantity = batchService.findBatchesByProductOrderBy(1, "C");
        List<Batch> listByDueDate = batchService.findBatchesByProductOrderBy(1, "F");
        List<Batch> listById = batchService.findBatchesByProductOrderBy(1, "");

        Assertions.assertEquals(list1, listByBatch);
        Assertions.assertEquals(list1, listByQuantity);
        Assertions.assertEquals(list2, listByDueDate);
        Assertions.assertEquals(list1, listById);
    }

    @Test
    public void shouldReturnTrueWhenBatchExists() {
        Batch batch1 = Batch.builder().batchNumber(1).build();
        Mockito.when(batchRepository.existsBatchByBatchNumber(batch1.getBatchNumber())).thenReturn(true);
        boolean returnValue = batchService.checkIfBatchExists(batch1);

        Assertions.assertEquals(true, returnValue);
    }

    @Test
    public void TestFindBatchesByDueDateGreaterThanEqualAndSectorEquals() {
        LocalDate now = LocalDate.now();
        Batch batch1 = Batch.builder().dueDate(now.plusDays(10)).build();
        Batch batch2 = Batch.builder().dueDate(now.plusDays(20)).build();
        List<Batch> expectedBatchList = new ArrayList<>(Arrays.asList(batch1, batch2));
        Integer expiringLimitDays = 25;
        LocalDate expiringLimitDate = now.plusDays(expiringLimitDays);
        Integer sectionId = 1;

        Mockito.when(batchRepository.findBatchesByDueDateGreaterThanEqualAndSectorEquals(expiringLimitDate, sectionId))
                .thenReturn(expectedBatchList);

        List<Batch> returnValue = batchService.findBatchesByDueDateGreaterThanEqualAndSectorEquals(expiringLimitDays, sectionId);

        Assertions.assertEquals(expectedBatchList.size(), returnValue.size());
        Assertions.assertEquals(expectedBatchList, returnValue);
    }

    /**
     * Deve retornar uma lista de lotes filtrados por dias até a dueDate, StorageType,
     * ordenado de forma crescente por DueDate.
     * @author Ana Preis
     */
    @Test
    void shouldfindBatchesFilteredByDueDateAndStorageType(){
        mockProduct2.setProductId(2);
        mockProduct2.setProductName("Test2");
        mockProduct2.setProductDescription("Product Test");
        mockProduct2.setProductType(StorageType.FRESH);

        mockProduct3.setProductId(3);
        mockProduct3.setProductName("Test3");
        mockProduct3.setProductDescription("Product Test");
        mockProduct3.setProductType(StorageType.REFRIGERATED);

        Batch mockBatch = Batch.builder()
                .batchNumber(1)
                .dueDate(LocalDate.of(2022, 6, 03))
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build();

        Batch mockBatch2 = Batch.builder()
                .batchNumber(2)
                .dueDate(LocalDate.of(2022, 6, 02))
                .inboundOrder(mockInboundOrder)
                .product(mockProduct2)
                .build();

        Batch mockBatch3 = Batch.builder()
                .batchNumber(3)
                .dueDate(LocalDate.of(2022, 6, 01))
                .inboundOrder(mockInboundOrder)
                .product(mockProduct3)
                .build();

        List<Batch> list1 = Arrays.asList(mockBatch2, mockBatch);
        List<Batch> list3 = Arrays.asList(mockBatch3);
        List<Batch> listAll = Arrays.asList(mockBatch, mockBatch2, mockBatch3);

        Mockito.when(batchCustomRepository.find(LocalDate.now().plusDays(40),
                StorageType.valueOf("FRESH"), "asc")).thenReturn(list1);
        Mockito.when(batchCustomRepository.find(LocalDate.now().plusDays(40),
                null, "asc")).thenReturn(listAll);
        Mockito.when(batchCustomRepository.find(null,
                StorageType.valueOf("REFRIGERATED"), "asc")).thenReturn(list3);
        Mockito.when(batchRepository.findAll()).thenReturn(listAll);

        List<Batch> listByBatch = batchService.findBatchesOrderBy(40, "FRESH", "asc");
        List<Batch> listNullCatgory = batchService.findBatchesOrderBy(40, null, "asc");
        List<Batch> listNullDays = batchService.findBatchesOrderBy(null, "REFRIGERATED", "asc");
        List<Batch> listOfAll = batchService.findBatchesOrderBy(null, null, null);

        Assertions.assertEquals(list1, listByBatch);
        Assertions.assertEquals(listAll, listNullCatgory);
        Assertions.assertEquals(list3, listNullDays);
        Assertions.assertEquals(listAll, listOfAll);
    }

    /**
     * Deve retornar um erro caso seja passado um StorageType diferente de nulo ou dos já existentes ou se for passado
     * algo diferente de "asc" ou "desc" no parametro order.
     * @author Ana Preis
     */
    @Test
    void shouldthrowErrorIfQueryIsInvalid(){
        String messageQuery = "Invalid query for category";
        String messageOrder = "Invalid query for order";

        RuntimeException exceptionCategory = Assertions.assertThrows(RuntimeException.class, () ->
                batchService.findBatchesOrderBy(40, "INVALIDCATEGORY", "asc"));
        RuntimeException exceptionOrder = Assertions.assertThrows(RuntimeException.class, () ->
                batchService.findBatchesOrderBy(40, "FRESH", "invalidorder"));

        assertThat(exceptionCategory.getMessage()).isEqualTo(messageQuery);
        assertThat(exceptionOrder.getMessage()).isEqualTo(messageOrder);
    }
}
