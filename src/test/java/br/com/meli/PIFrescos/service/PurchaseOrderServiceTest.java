package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
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
    PurchaseOrderRepository purchaseOrderRepository;

    @InjectMocks
    PurchaseOrderService purchaseOrderService;

    private Product product1 = new Product();
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private List<ProductsCart> productsCartList = new ArrayList<>();
    private ProductsCart productsCart1 = new ProductsCart();
    private ProductsCart productsCart2 = new ProductsCart();
    private List<PurchaseOrder> purchaseOrderList = new ArrayList<>();

    @BeforeEach
    void setup() {
        product1.setProductId(1);
        product1.setProductName("Banana");
        product1.setProductType(FRESH);
        product1.setProductDescription("descriptionBanana");

        productsCart1.setId(1);
        productsCart1.setBatch(new Batch());
        productsCart1.setQuantity(5);

        productsCart2.setId(2);
        productsCart2.setBatch(new Batch());
        productsCart2.setQuantity(10);

        productsCartList = Arrays.asList(productsCart1, productsCart2);

        purchaseOrder.setId(1);
        purchaseOrder.setUser(new User());
        purchaseOrder.setDate(LocalDate.of(2022, 1, 8));
        purchaseOrder.setOrderStatus(OPENED);
        purchaseOrder.setCartList(productsCartList);

        purchaseOrderList = Arrays.asList(purchaseOrder);
    }

    @Test
    void shouldSavePurchaseOrder(){
        Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.empty());

        PurchaseOrder savedPurchaseOrder = purchaseOrderService.save(purchaseOrder);

        assertEquals(purchaseOrder, savedPurchaseOrder);
    }

    @Test
    void shouldNotSavePurchaseOrder(){
        String message = "PurchaseOrder already exists!";
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.ofNullable(purchaseOrder));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> purchaseOrderService.save(purchaseOrder));

        assertThat(exception.getMessage()).isEqualTo(message);
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
        PurchaseOrder expectedPurchaseOrder = purchaseOrder;
        expectedPurchaseOrder.setOrderStatus(FINISHED);
        OrderStatus newOrderStatus = FINISHED;
        Mockito.when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.ofNullable(purchaseOrder));
        Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updateOrderStatus(newOrderStatus, 1);

        verify(purchaseOrderRepository).deleteById(any());
        assertEquals(expectedPurchaseOrder, updatedPurchaseOrder);
    }

    @Test
    void shouldNotFindPurchaseOrderAndUpdate(){
        String message = "PurchaseOrder not found";
        Mockito.when(purchaseOrderRepository.findById(3)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> purchaseOrderService.updateOrderStatus(FINISHED, 3));

        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
