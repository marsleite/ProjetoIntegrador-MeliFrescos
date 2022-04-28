package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.config.handler.ProductCartException;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import br.com.meli.PIFrescos.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static br.com.meli.PIFrescos.models.OrderStatus.FINISHED;
import static br.com.meli.PIFrescos.models.OrderStatus.OPENED;
import static br.com.meli.PIFrescos.models.StorageType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Ana Preis
 */
@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    BatchRepository batchRepository;

    @Mock
    IBatchService batchService;

    @InjectMocks
    PurchaseOrderService purchaseOrderService;

    private Product product1 = new Product();
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private Batch mockBatch1 = new Batch();
    private Batch mockBatch2 = new Batch();
    private Batch mockBatch3 = new Batch();
    private List<ProductsCart> productsCartList = new ArrayList<>();
    private ProductsCart productsCart1 = new ProductsCart();
    private ProductsCart productsCart2 = new ProductsCart();
    private List<PurchaseOrder> purchaseOrderList = new ArrayList<>();
    private User user1 = new User();

    @BeforeEach
    void setup() {
        user1.setId(1);

        product1.setProductId(1);
        product1.setProductName("Banana");
        product1.setProductType(FRESH);
        product1.setProductDescription("descriptionBanana");

        mockBatch1 = Batch.builder()
                .batchNumber(1)
                .product(product1)
                .currentQuantity(10)
                .build();

        mockBatch2 = Batch.builder()
                .batchNumber(2)
                .product(product1)
                .currentQuantity(10)
                .build();

        mockBatch3 = Batch.builder()
                .batchNumber(3)
                .product(product1)
                .currentQuantity(0)
                .build();

        productsCart1.setId(1);
        productsCart1.setBatch(mockBatch1);
        productsCart1.setQuantity(5);

        productsCart2.setId(2);
        productsCart2.setBatch(mockBatch2);
        productsCart2.setQuantity(9);

        productsCartList = Arrays.asList(productsCart1, productsCart2);

        purchaseOrder.setId(1);
        purchaseOrder.setUser(user1);
        purchaseOrder.setDate(LocalDate.of(2022, 1, 8));
        purchaseOrder.setOrderStatus(OPENED);
        purchaseOrder.setCartList(productsCartList);

        purchaseOrderList = Arrays.asList(purchaseOrder);
    }

    @Test
    void shouldSavePurchaseOrder(){
        Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
        Mockito.when(batchRepository.findByBatchNumber(1)).thenReturn(mockBatch1);
        Mockito.when(batchRepository.findByBatchNumber(2)).thenReturn(mockBatch2);
        PurchaseOrder savedPurchaseOrder = purchaseOrderService.save(purchaseOrder);

        assertEquals(purchaseOrder, savedPurchaseOrder);
    }

    @Test
    void shouldSavePurchaseOrderWhenUserAlreadyHasAnOrder(){
        Mockito.when(purchaseOrderRepository.getPurchaseOrdersByUserIdAndOrderStatusIsOPENED(any())).thenReturn(purchaseOrder);
        Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
        Mockito.when(batchRepository.findByBatchNumber(1)).thenReturn(mockBatch1);
        Mockito.when(batchRepository.findByBatchNumber(2)).thenReturn(mockBatch2);
        PurchaseOrder savedPurchaseOrder = purchaseOrderService.save(purchaseOrder);

        assertEquals(purchaseOrder, savedPurchaseOrder);
    }

    @Test
    void shouldNotValidatePurchaseOrder(){
        productsCart1.setBatch(mockBatch3);
        Mockito.when(batchRepository.findByBatchNumber(3)).thenReturn(mockBatch3);
        Mockito.when(batchRepository.findByBatchNumber(2)).thenReturn(mockBatch2);

        ProductCartException exception = Assertions.assertThrows(ProductCartException.class, () -> purchaseOrderService.save(purchaseOrder));

        assertEquals(exception.getErrorFormsDtoList().size(),1);
    }

    @Test
    void shouldGetAll(){
        Mockito.when(purchaseOrderRepository.findAll()).thenReturn(purchaseOrderList);

        List<PurchaseOrder> newPurchaseOrderList = purchaseOrderService.getAll();

        assertEquals(purchaseOrderList, newPurchaseOrderList);
    }

    @Test
    void shouldNotGetAll(){
        String message = "PurchaseOrder list is empty";
        Mockito.when(purchaseOrderRepository.findAll()).thenReturn(new ArrayList<>());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> purchaseOrderService.getAll());

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldGetById(){
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.ofNullable(purchaseOrder));

        PurchaseOrder newPurchaseOrder = purchaseOrderService.getById(1);

        assertEquals(purchaseOrder, newPurchaseOrder);
    }

    @Test
    void shouldNotGetById(){
        String message = "PurchaseOrder not found";
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> purchaseOrderService.getById(1));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldDelete(){
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.ofNullable(purchaseOrder));

        purchaseOrderService.delete(1);

        verify(purchaseOrderRepository).deleteById(any());
    }

    @Test
    void shouldNotDelete(){
        String message = "PurchaseOrder not found";
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> purchaseOrderService.delete(1));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldUpdateOrder(){
        mockBatch3.setCurrentQuantity(5);
        PurchaseOrder expectedPurchaseOrder = purchaseOrder;
        expectedPurchaseOrder.setOrderStatus(OPENED);
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.ofNullable(purchaseOrder));
        Mockito.when(batchService.updateCurrentQuantity(productsCart1.getQuantity(), productsCart1.getBatch())).thenReturn(mockBatch3);

        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updateOrderStatus(1);

        assertEquals(expectedPurchaseOrder, updatedPurchaseOrder);
    }

    @Test
    void shouldNotFindPurchaseOrderAndUpdate(){
        String message = "PurchaseOrder not found";
        Mockito.when(purchaseOrderRepository.findById(3)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> purchaseOrderService.updateOrderStatus(3));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera receber uma lista de produtos.
     */
    @Test
    void shouldReturnProductsByOrderId() {
        Mockito.when(purchaseOrderRepository.findById(1)).thenReturn(Optional.ofNullable(purchaseOrder));

       List<Product> list = purchaseOrderService.findProductsByOrderId(1);
       assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void shouldGetPurchaseByUser(){
        Mockito.when(purchaseOrderRepository.findByUser(user1)).thenReturn(purchaseOrder);

        PurchaseOrder response = purchaseOrderService.findPurchaseByUser(user1);

        assertEquals(purchaseOrder, response);
    }
}
