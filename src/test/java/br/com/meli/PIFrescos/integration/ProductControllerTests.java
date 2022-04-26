package br.com.meli.PIFrescos.integration;

import br.com.meli.PIFrescos.dtos.InboundOrderDTO;
import br.com.meli.PIFrescos.dtos.ProductDTO;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.ProductRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

/**
 * @author Antonio Hugo
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTests {

    @MockBean
    private ProductRepository productRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    Product mockProduct = new Product();
    Product mockProduct2 = new Product();

    String payload = "{ \n"
            + " \"productName\": \"Queijo prato\","
            + " \"productType\": \"FRESH\","
            + " \"productDescription\": \"queijo do tipo Mussarela\""
            + "}";

    @BeforeEach
    public void setUp() {
        mockProduct.setProductId(1);
        mockProduct.setProductType(StorageType.FRESH);
        mockProduct.setProductName("Uva");
        mockProduct.setProductDescription("Mock description");

        mockProduct2.setProductId(2);
        mockProduct2.setProductType(StorageType.FRESH);
        mockProduct2.setProductName("Maça");
        mockProduct2.setProductDescription("Mock description");


    }

    /**
     * @author Antonio Hugo
     * Valida se será retornado todos os produtos
     */
    @Test
    public void shouldReturnAllProducts() throws Exception {

        List<Product> products = new ArrayList<>();

        products.add(mockProduct);
        products.add(mockProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/products/"))
                .andExpect(status().isOk())
                .andReturn();
    }
    /**
     * @author Antonio Hugo
     * Valida a criação do um produto
     */

    @Test
    public void shouldCreateProduct() throws Exception {

        ProductDTO result = objectMapper.readValue(payload, ProductDTO.class);
        Mockito.when(productRepository.save(any())).thenReturn(result.convert());

        mockMvc.perform(post("/fresh-products/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Queijo prato"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Valida a criação a atualizacao um produto
     */

    @Test
    public void shouldUpdateProduct() throws Exception {
        ProductDTO result = objectMapper.readValue(payload, ProductDTO.class);
        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(result.convert()));

        String payloadUpdated = "{ \n"
                + " \"productName\": \"Queijo Mussarela\","
                + " \"productType\": \"FRESH\","
                + " \"productDescription\": \"queijo do tipo Mussarela\""
                + "}";

        ProductDTO resultUpdate = objectMapper.readValue(payloadUpdated, ProductDTO.class);
        Mockito.when(productRepository.save(any())).thenReturn(resultUpdate.convert());

        mockMvc.perform(put("/fresh-products/products/1")
                .contentType(MediaType.APPLICATION_JSON).content(payloadUpdated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Queijo Mussarela"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Valida se o produto será removido
     */
    @Test
    public void shouldDeleteProductById() throws Exception {
        ProductDTO result = objectMapper.readValue(payload, ProductDTO.class);
        Product  product = result.convert();
        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(product));

        mockMvc.perform(delete("/fresh-products/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    /**
     * @author Antonio Hugo
     * Valida se será retornado 404 quando a URI estiver errada
     */
    @Test
    public void shouldStatusCode404NotFound() throws Exception {

        mockMvc.perform(get("/not_exists"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
