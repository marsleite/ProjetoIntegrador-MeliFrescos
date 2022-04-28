package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Antonio Hugo
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PurchaseOrderControllerIT {

    @MockBean
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String payload = "{ \n"
            + " \"userId\": 1,"
            + " \"orderStatus\": \"OPENED\","
            + " \"cartList\": ["
            + " { "
            +       " \"batchId\": 1,"
            +       " \"quantity\": 5"
            + " },"
            + " {"
            +     " \"batchId\": 2,"
            +     " \"quantity\": 5"
            + " }"
            + " ]"
            + " }";

    PurchaseOrder purchaseOrderMock = new PurchaseOrder();
    User userMock = new User();
    ProductsCart productsCartMock = new ProductsCart();
    ProductsCart productsCartMock2 = new ProductsCart();
    @BeforeEach
    public void setUp() {

        userMock.setId(1);
        userMock.setFullname("John Doe");
        userMock.setEmail("john@mercadolivre.com.br");
        userMock.setPassword("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy");
        userMock.setRole(UserRole.BUYER);

        productsCartMock.setId(1);
        productsCartMock.setQuantity(5);
        productsCartMock.setBatch(Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(50)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .build());

        productsCartMock2.setId(2);
        productsCartMock2.setQuantity(5);
        productsCartMock2.setBatch(Batch.builder()
                .batchNumber(2)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(10)
                .currentQuantity(50)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .build());

        purchaseOrderMock.setId(1);
        purchaseOrderMock.setOrderStatus(OrderStatus.OPENED);
        purchaseOrderMock.setDate(LocalDate.now());
        purchaseOrderMock.setUser(userMock);
        purchaseOrderMock.setCartList(List.of(productsCartMock, productsCartMock2));

    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os produtos.
     */
    @Test
    public void shouldCreatePurchaseOrder() throws Exception {
        PurchaseOrderForm result = objectMapper.readValue(payload, PurchaseOrderForm.class);
        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(PurchaseOrderForm.convertToEntity(result));

        mockMvc.perform(post("/fresh-products/orders")
            .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk())
                .andReturn();
    }
}
