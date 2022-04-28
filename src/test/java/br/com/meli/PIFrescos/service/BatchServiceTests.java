package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


    Product mockProduct = new Product();
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
}
