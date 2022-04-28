package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.config.security.TokenService;
import br.com.meli.PIFrescos.controller.forms.PurchaseOrderForm;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.repository.PurchaseOrderRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.yaml.snakeyaml.tokens.Token;

import java.beans.Encoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

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
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


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
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userMock.setId(1);
        userMock.setFullname("John Doe");
        userMock.setEmail("john@mercadolivre.com.br");
        userMock.setPassword("123456");
        userMock.setRole(UserRole.ADMIN);

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
     * Este teste espera criar um novo pedido.
     */
//    @Test
////    @WithMockUser(username = "John Doe", roles = { "USER", "ADMIN"}, password = "123456")
//    @WithMockUser("test")
//    public void shouldCreatePurchaseOrder() throws Exception {
////        PurchaseOrderForm result = objectMapper.readValue(payload, PurchaseOrderForm.class);
////        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNZWxpRnJlc2NvcyIsInN1YiI6IjQiLCJpYXQiOjE2NTExODEwNzgsImV4cCI6MTY1MTI2NzQ3OH0.K7ZCAYBcvmnLke7KwSdUDS7sBM0W8d--5-dWF1IUshQ";
////        Mockito.when(userRepository.findAll()).thenReturn(List.of(userMock));
//        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrderMock);
//        Mockito.when(purchaseOrderRepository.save(any())).thenReturn(purchaseOrderMock);
//
//        mockMvc.perform(get("/fresh-products")
//                .contentType(MediaType.APPLICATION_JSON).content(payload))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalPrice").value(""))
//                .andReturn();
//    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os produtos.
     */
    @Test
    @WithMockUser("test")
    public void shouldReturnAllProducts() throws Exception {
        Product mockProduct = new Product();
        Product mockProduct2 = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductType(StorageType.FRESH);
        mockProduct.setProductName("Uva");
        mockProduct.setProductDescription("Mock description");

        mockProduct2.setProductId(3);
        mockProduct2.setProductType(StorageType.REFRIGERATED);
        mockProduct2.setProductName("Maça");
        mockProduct2.setProductDescription("Mock description");

        List<Product> products = new ArrayList<>();

        products.add(mockProduct);
        products.add(mockProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].productId").value("1"))
                .andReturn();
    }
}
