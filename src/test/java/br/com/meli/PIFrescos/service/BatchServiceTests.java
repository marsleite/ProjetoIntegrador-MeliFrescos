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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void shouldBeAbleReturnBatch(){

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

    @Test
    public void shouldNotBeAbleToFindBatchIfDoesntExist() {

        Assertions.assertThrows(RuntimeException.class, () -> batchService.findBatchById(100));

    }

    @Test
    public void shouldBeAbleReturnBatchList(){
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


}
