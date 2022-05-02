package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.BatchStockDTO;
import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.BatchCustomRepository;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ListAssert.*;
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
    Product mockProduct3 = new Product();
    ProductsCart productsCartMock = new ProductsCart();
    ProductsCart productsCartMock2 = new ProductsCart();
    InboundOrder inboundOrderMock = new InboundOrder();
    Section sectionMock = new Section();
    @BeforeEach
    public void setUp() throws Exception {
        sectionMock.setCurrentCapacity(100);
        sectionMock.setStorageType(StorageType.FRESH);
        inboundOrderMock.setSection(sectionMock);

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
        mockProduct2.setProductType(StorageType.FROZEN);
        mockProduct2.setProductName("Peixe");
        mockProduct2.setProductDescription("Mock description");

        mockProduct3.setProductId(3);
        mockProduct3.setProductType(StorageType.FRESH);
        mockProduct3.setProductName("Uva");
        mockProduct3.setProductDescription("Mock description");

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
                .inboundOrder(inboundOrderMock)
                .build());

        productsCartMock2.setId(2);
        productsCartMock2.setQuantity(1);
        productsCartMock2.setBatch(Batch.builder()
                .batchNumber(2)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.parse("2022-05-30"))
                .product(mockProduct2)
                .unitPrice(BigDecimal.valueOf(10.0))
                .inboundOrder(inboundOrderMock)
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
     * Este teste espera ser retornado todos os lotes cadatrados.
     */
    @Test
    public void shouldGetBatchesByCategoryWithoutFilter() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();

        batches.add(productsCartMock.getBatch());
        batches.add(productsCartMock2.getBatch());
        Mockito.when(productRepository.findAll()).thenReturn(List.of(mockProduct, mockProduct2));
        Mockito.when(batchRepository.findAll()).thenReturn(batches);

        MvcResult result = mockMvc.perform(get("/fresh-products/due-date/list")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<BatchStockDTO>> typeReference = new TypeReference<List<BatchStockDTO>>() {};
        objectMapper.registerModule(new JavaTimeModule());
        List<BatchStockDTO> batchs = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(batchs).isNotNull();
        assertThat(batchs).hasSize(2);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os lotes de uma determinada seção
     */
    @Test
    public void shouldGetBatchesBySectionAndDueDateLessThan() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();

        batches.add(productsCartMock.getBatch());
        batches.add(productsCartMock2.getBatch());
        Mockito.when(batchRepository.findBatchesByDueDateGreaterThanEqualAndSectorEquals(any(), any())).thenReturn(batches);

        MvcResult result = mockMvc.perform(get("/fresh-products/due-date?sectionId=1&expiringLimit=10")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<BatchStockDTO>> typeReference = new TypeReference<List<BatchStockDTO>>() {};
        objectMapper.registerModule(new JavaTimeModule());
        List<BatchStockDTO> batchs = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(batchs).isNotNull();
        assertThat(batchs).hasSize(2);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os produtos.
     */
    @Test
    public void shouldGetBatchesByCategoryFilterDay() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();
        batches.add(productsCartMock.getBatch());
        batches.add(productsCartMock2.getBatch());
        Mockito.when(batchCustomRepository.find(any(), any(), any())).thenReturn(batches);

        MvcResult result = mockMvc.perform(get("/fresh-products/due-date/list?days=10")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<BatchStockDTO>> typeReference = new TypeReference<List<BatchStockDTO>>() {};
        objectMapper.registerModule(new JavaTimeModule());
        List<BatchStockDTO> batchs = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(batchs).isNotNull();
        assertThat(batchs).hasSize(2);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos de um determinado typo.
     */
    @Test
    public void shouldGetBatchesByCategoryFilterCategory() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();
        sectionMock.setStorageType(StorageType.FROZEN);
        batches.add(productsCartMock2.getBatch());
        Mockito.when(batchCustomRepository.find(any(), any(), any())).thenReturn(batches);

        MvcResult result = mockMvc.perform(get("/fresh-products/due-date/list?category=FROZEN")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result);
        TypeReference<List<BatchStockDTO>> typeReference = new TypeReference<List<BatchStockDTO>>() {};
        objectMapper.registerModule(new JavaTimeModule());
        List<BatchStockDTO> batchs = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(batchs).isNotNull();
        assertThat(batchs).extracting("productType").contains(StorageType.FROZEN);
        assertThat(batchs).hasSize(1);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado os lotes de produtos filtrado para vencer, categoria e ordenado de forma ascendente.
     */
    @Test
    public void shouldGetBatchesByCategoryFilterDaysCategoryOrder() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        List<Batch> batches = new ArrayList<>();

        batches.add(productsCartMock.getBatch());
        batches.add(productsCartMock2.getBatch());
        Mockito.when(productRepository.findAll()).thenReturn(List.of(mockProduct, mockProduct2));
        Mockito.when(batchCustomRepository.find(any(), any(), any())).thenReturn(batches);

        MvcResult result = mockMvc.perform(get("/fresh-products/due-date/list?days=15&category=FRESH&order=asc")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<BatchStockDTO>> typeReference = new TypeReference<List<BatchStockDTO>>() {};
        objectMapper.registerModule(new JavaTimeModule());

        List<BatchStockDTO> batchs = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(batchs).isNotNull();
        assertThat(batchs).extracting("productType").contains(StorageType.FRESH, StorageType.FRESH);
        assertThat(batchs).hasSize(2);
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser receber 400 quando o tipo da ordenação for inválida.
     */
    @Test
    public void shouldGetBatchesByCategoryFilterDaysCategoryInvalidOrder() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

         mockMvc.perform(get("/fresh-products/due-date/list?days=15&category=FRESH&order=any")
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid query for order"))
                .andReturn();

    }
    /**
     * @author Antonio Hugo
     * Este teste espera ser receber 400 quando parametros estiverem vazios.
     */
    @Test
    public void shouldGetBatchesBySectionAndDueDateLessThanWithInvalidParam() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        mockMvc.perform(get("/fresh-products/due-date")
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'sectionId' for method parameter type Integer is not present"))
                .andReturn();

    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser receber 400 quando o parametro de experingLimit estiver vazio.
     */
    @Test
    public void shouldGetBatchesBySectionAndDueDateLessThanWithInvalidExpiringLimit() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        mockMvc.perform(get("/fresh-products/due-date?sectionId=1")
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'expiringLimit' for method parameter type Integer is not present"))
                .andReturn();

    }
}
