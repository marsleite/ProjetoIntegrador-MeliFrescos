package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.BatchCustomRepository;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Antonio Hugo
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class BatchControllerIT {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BatchRepository batchRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private BatchCustomRepository batchCustomRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String userLogin = "{"
            + "\"email\": \"meli@gmail.com\", "
            + "\"password\": \"123456\""
            + "}";

    private String accessToken;

    User userMock = new User();
    Profile profile= new Profile();
    Product mockProduct = new Product();
    Product mockProduct2 = new Product();
    ProductsCart productsCartMock = new ProductsCart();
    ProductsCart productsCartMock2 = new ProductsCart();

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

        this.accessToken = this.userLogin();

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
                .dueDate(LocalDate.parse("2022-05-30"))
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
                .dueDate(LocalDate.parse("2022-05-30"))
                .product(mockProduct2)
                .unitPrice(BigDecimal.valueOf(10.0))
                .build());

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
     * Este teste espera ser retornado todos os produtos.
     */
//    @Test
//    public void shouldGetBatchesByCategoryFilteredDay() throws Exception {
//
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));
//
//        List<Batch> batches = new ArrayList<>();
//
//        batches.add(productsCartMock.getBatch());
//        batches.add(productsCartMock2.getBatch());
////        System.out.println(batches);
//        Mockito.when(productRepository.findAll()).thenReturn(List.of(mockProduct, mockProduct2));
//        Mockito.when(batchRepository.findAll()).thenReturn(batches);
////        Mockito.when(batchCustomRepository.find(any(), any(), any())).thenReturn(batches);
//
//        mockMvc.perform(get("/fresh-products/due-date/list")
//                .header("Authorization", accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*]").value("ss"))
//                .andReturn();
//    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os produtos.
     */
    @Test
    public void shouldgetBatchesBySectionAndDueDateLessThan() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();

        batches.add(productsCartMock.getBatch());
        batches.add(productsCartMock2.getBatch());
//        System.out.println(batches);
//        Mockito.when(productRepository.findAll()).thenReturn(List.of(mockProduct, mockProduct2));
        Mockito.when(batchRepository.findBatchesByDueDateGreaterThanEqualAndSectorEquals(any(), any())).thenReturn(batches);
//        Mockito.when(batchCustomRepository.find(any(), any(), any())).thenReturn(batches);

        mockMvc.perform(get("/fresh-products/due-date/duedate?sectionId=1&expiringLimit=10")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").value("ss"))
                .andReturn();
    }
}
