package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BatchRepository batchRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String payload = "{ \n"
            + " \"userId\": 1,"
            + " \"orderStatus\": \"OPENED\","
            + " \"cartList\": ["
            + " { "
            +       " \"batchId\": 1,"
            +       " \"quantity\": 1"
            + " },"
            + " {"
            +     " \"batchId\": 2,"
            +     " \"quantity\": 1"
            + " }"
            + " ]"
            + " }";

    String userLogin = "{"
            + "\"email\": \"meli@gmail.com\", "
            + "\"password\": \"123456\""
            + "}";

    private String accessToken;

    User userMock = new User();
    Profile profile= new Profile();
    PurchaseOrder purchaseOrderMock = new PurchaseOrder();
    ProductsCart productsCartMock = new ProductsCart();
    ProductsCart productsCartMock2 = new ProductsCart();
    ProductsCart productsCartMock3 = new ProductsCart();
    Product mockProduct = new Product();
    Product mockProduct2 = new Product();

    @BeforeEach
    public void setUp() throws Exception {

        profile.setId(1L);
        profile.setName("ADMIN");
        userMock.setId(1);
        userMock.setFullname("John Doe");
        userMock.setEmail("john@mercadolivre.com.br");
        userMock.setPassword("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy");
        userMock.setProfiles(List.of(profile));
        userMock.setRole(UserRole.ADMIN);

        mockProduct.setProductId(1);
        mockProduct.setProductType(StorageType.FRESH);
        mockProduct.setProductName("Uva");
        mockProduct.setProductDescription("Mock description");

        mockProduct2.setProductId(2);
        mockProduct2.setProductType(StorageType.FRESH);
        mockProduct2.setProductName("Maça");
        mockProduct2.setProductDescription("Mock description");

        productsCartMock.setId(1);
        productsCartMock.setQuantity(1);
        productsCartMock.setBatch(Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(50)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .product(mockProduct)
                .unitPrice(BigDecimal.valueOf(20.0))
                .build());

        productsCartMock2.setId(2);
        productsCartMock2.setQuantity(1);
        productsCartMock2.setBatch(Batch.builder()
                .batchNumber(2)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(10)
                .currentQuantity(50)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .product(mockProduct2)
                .unitPrice(BigDecimal.valueOf(10.0))
                .build());

        productsCartMock3.setId(2);
        productsCartMock3.setQuantity(2);
        productsCartMock3.setBatch(Batch.builder()
                .batchNumber(2)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(10)
                .currentQuantity(50)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .product(mockProduct2)
                .unitPrice(BigDecimal.valueOf(10.0))
                .build());

        purchaseOrderMock.setId(1);
        purchaseOrderMock.setOrderStatus(OrderStatus.OPENED);
        purchaseOrderMock.setDate(LocalDate.now());
        purchaseOrderMock.setUser(userMock);
        purchaseOrderMock.setCartList(List.of(productsCartMock, productsCartMock2));

    }

    /**
     * This method returned the mock user token.
     * @return String
     * @author Antonio Hugo
     *
     */
    private String userLogin() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));
        MvcResult result = mockMvc.perform(post("/auth")
                .contentType("application/json").content(userLogin))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<TokenDto> typeReference = new TypeReference<TokenDto>() {};
        TokenDto token = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        return "Bearer " + token.getToken();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera criar um novo pedido.
     */
    @Test
    public void shouldCreatePurchaseOrder() throws Exception {
        this.accessToken = this.userLogin();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(purchaseOrderRepository.getPurchaseOrdersByUserIdAndOrderStatusIsOPENED(any())).thenReturn(null);
        Mockito.when(batchRepository.findByBatchNumber(any())).thenReturn(productsCartMock.getBatch());
        Mockito.when(batchRepository.findByBatchNumber(any())).thenReturn(productsCartMock2.getBatch());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(userMock));
        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrderMock);

        mockMvc.perform(post("/fresh-products/orders")
                .header("Authorization",  accessToken)
                .contentType("application/json").content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(30.0))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera receber 400 quando o paylod for vazio.
     */
    @Test
    public void shouldCreatePurchaseOrderInvalidPayload() throws Exception {
        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrderMock);

        mockMvc.perform(post("/fresh-products/orders")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera receber todos os produtos do pedido.
     */
    @Test
    public void shouldGetProductsOnPurchaseOrder() throws Exception {
        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(purchaseOrderRepository.findByUser(any())).thenReturn(purchaseOrderMock);
        Mockito.when(purchaseOrderRepository.getById(any())).thenReturn(purchaseOrderMock);
        Mockito.when(purchaseOrderRepository.findById(any())).thenReturn(Optional.ofNullable(purchaseOrderMock));

        Mockito.when(purchaseOrderRepository.getPurchaseOpenedByUserId(any())).thenReturn(List.of(purchaseOrderMock));

        mockMvc.perform(get("/fresh-products/orders")
                .header("Authorization", accessToken)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseOrderId").value(1))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera 404 quando existe produtos no pedido.
     */
    @Test
    public void shouldReturn404WhenNotExistProductsOnPurchaseOrder() throws Exception {
        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(purchaseOrderRepository.findByUser(any())).thenReturn(purchaseOrderMock);
        Mockito.when(purchaseOrderRepository.getById(any())).thenReturn(purchaseOrderMock);
        mockMvc.perform(get("/fresh-products/orders")
                .header("Authorization", accessToken)
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No OPENED purchase cart"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera atualizar um pedido.
     */
    @Test
    public void shouldUpdatePurchaseOrder() throws Exception {

        String payloadUpdate = "{ \n"
                + " \"cartList\": ["
                + " { "
                +       " \"batchId\": 1,"
                +       " \"quantity\": 1"
                + " },"
                + " { "
                +       " \"batchId\": 2,"
                +       " \"quantity\": 2"
                + " }"
                + " ]"
                + " }";

        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(purchaseOrderRepository.findByUserId(any())).thenReturn(Optional.ofNullable(purchaseOrderMock));
        Mockito.when(batchRepository.findByBatchNumber(any())).thenReturn(productsCartMock.getBatch());
        Mockito.when(batchRepository.findByBatchNumber(any())).thenReturn(productsCartMock2.getBatch());

        purchaseOrderMock.setCartList(List.of(productsCartMock, productsCartMock3));

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(userMock));
        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrderMock);

        mockMvc.perform(put("/fresh-products/orders")
                .header("Authorization",  accessToken)
                .contentType("application/json").content(payloadUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(40.0))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera receber 404 quando a quantidade de produto solicitado for menor que a quantidade em estoque.
     */
    @Test
    public void shouldReturn404WhenInsufficientQuantityProductsOnPurchaseOrder() throws Exception {

        String payloadUpdate = "{ \n"
                + " \"cartList\": ["
                + " { "
                +       " \"batchId\": 1,"
                +       " \"quantity\": 1"
                + " },"
                + " { "
                +       " \"batchId\": 2,"
                +       " \"quantity\": 2"
                + " }"
                + " ]"
                + " }";

        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        productsCartMock2.getBatch().setCurrentQuantity(0);
        Mockito.when(batchRepository.findByBatchNumber(1)).thenReturn(productsCartMock.getBatch());
        Mockito.when(batchRepository.findByBatchNumber(2)).thenReturn(productsCartMock2.getBatch());

        mockMvc.perform(put("/fresh-products/orders")
                .header("Authorization",  accessToken)
                .contentType("application/json").content(payloadUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].error").value("Insufficient quantity of product on batch"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera finalizar um pedido aberto.
     */
    @Test
    public void shouldUpdateOrderToFinish() throws Exception {

        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
        Mockito.when(purchaseOrderRepository.getPurchaseOpenedByUserId(any())).thenReturn(List.of(purchaseOrderMock));

        purchaseOrderMock.setOrderStatus(OrderStatus.FINISHED);

        Mockito.when(purchaseOrderRepository.findById(any())).thenReturn(Optional.ofNullable(purchaseOrderMock));

        mockMvc.perform(put("/fresh-products/orders/finish?idOrder=1")
                .header("Authorization",  accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(30.0))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera receber 404 quando tentar finalizar um pedido que não existe.
     */
    @Test
    public void shouldReturn404FinishOrderNotExist() throws Exception {

        this.accessToken = this.userLogin();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(purchaseOrderRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/fresh-products/orders/finish?idOrder=1")
                .header("Authorization",  accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No OPENED purchase cart"))
                .andReturn();
    }

}
