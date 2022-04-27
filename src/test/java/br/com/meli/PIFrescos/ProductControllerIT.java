package br.com.meli.PIFrescos;

import br.com.meli.PIFrescos.controller.forms.ProductForm;
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
public class ProductControllerIT {
    @MockBean
    private ProductRepository productRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    Product mockProduct = new Product();
    Product mockProduct2 = new Product();
    Product mockProduct3 = new Product();

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

        mockProduct3.setProductId(3);
        mockProduct3.setProductType(StorageType.REFRIGERATED);
        mockProduct3.setProductName("Maça");
        mockProduct3.setProductDescription("Mock description");

        mockProduct2.setProductId(2);
        mockProduct2.setProductType(StorageType.FROZEN);
        mockProduct2.setProductName("Peixe");
        mockProduct2.setProductDescription("Mock description");
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado todos os produtos.
     */
    @Test
    public void shouldReturnAllProducts() throws Exception {

        List<Product> products = new ArrayList<>();

        products.add(mockProduct);
        products.add(mockProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/"))
                .andExpect(status().isOk())
                .andReturn();
    }
    /**
     * @author Antonio Hugo
     * Este teste espera criar do um produto.
     */

    @Test
    public void shouldCreateProduct() throws Exception {

        ProductForm result = objectMapper.readValue(payload, ProductForm.class);
        Mockito.when(productRepository.save(any())).thenReturn(result.convert());

        mockMvc.perform(post("/fresh-products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Queijo prato"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera que seja atualizado um produto existente.
     */

    @Test
    public void shouldUpdateProduct() throws Exception {
        ProductForm result = objectMapper.readValue(payload, ProductForm.class);
        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(result.convert()));

        String payloadUpdated = "{ \n"
                + " \"productName\": \"Queijo Mussarela\","
                + " \"productType\": \"FRESH\","
                + " \"productDescription\": \"queijo do tipo Mussarela\""
                + "}";

        ProductForm  resultUpdate = objectMapper.readValue(payloadUpdated, ProductForm.class);
        Mockito.when(productRepository.save(any())).thenReturn(resultUpdate.convert());

        mockMvc.perform(put("/fresh-products/1")
                .contentType(MediaType.APPLICATION_JSON).content(payloadUpdated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Queijo Mussarela"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera que produto será removido.
     */
    @Test
    public void shouldDeleteProductById() throws Exception {
        ProductForm result = objectMapper.readValue(payload, ProductForm.class);
        Product  product = result.convert();
        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(product));

        mockMvc.perform(delete("/fresh-products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado 404 quando a URI estiver incorreta.
     */
    @Test
    public void shouldStatusCode404NotFoundWhenPathNotExits() throws Exception {

        mockMvc.perform(get("/not_exists"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado 404 quando a lista de produto estiver vazia.
     */
    @Test
    public void shouldReturnStatusCode404NotFoundWhenProductListIsEmpty() throws Exception {

        List<Product> products = new ArrayList<>();

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product list is empty"))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado 404 quando o id do produto não for encontrado.
     */
    @Test
    public void shouldReturnStatusCode404NotFoundWhenProductDoesNotExist() throws Exception {

        mockMvc.perform(delete("/fresh-products/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado 400, quando o nome do produto enviado estiver vazio.
     */
    @Test
    public void shouldReturnStatusCode400BadRequestWhenProductNameIsEmpty() throws Exception {

        String payloadInvalid = "{ \n"
                + " \"productName\": \"\","
                + " \"productType\": \"FRESH\","
                + " \"productDescription\": \"queijo do tipo Mussarela\""
                + "}";

        mockMvc.perform(post("/fresh-products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadInvalid))
                .andExpect(status().isBadRequest());
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado 400 quando tipo do produto for inválido.
     */
    @Test
    public void shouldReturnStatusCode400BadRequestWhenProductTypeIsInvalid() throws Exception {

        String payloadInvalid = "{ \n"
                + " \"productName\": \"Queijo Brie\","
                + " \"productType\": \"ANY\","
                + " \"productDescription\": \"queijo francese\""
                + "}";

        mockMvc.perform(post("/fresh-products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadInvalid))
                .andExpect(status().isBadRequest());
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado apenas os produtos do tipo FS.
     */
    @Test
    public void shouldReturnFilteredProductsByTypeFS() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(mockProduct);
        products.add(mockProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/list?querytype=FS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].productId").value(1))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado apenas os produtos do tipo FF.
     */
    @Test
    public void shouldReturnFilteredProductsByTypeFF() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(mockProduct);
        products.add(mockProduct2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/list?querytype=FF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].productId").value(2))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado apenas os produtos do tipo RF.
     */
    @Test
    public void shouldReturnFilteredProductsByTypeRF() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(mockProduct);
        products.add(mockProduct2);
        products.add(mockProduct3);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/list?querytype=RF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].productId").value(3))
                .andReturn();
    }

    /**
     * @author Antonio Hugo
     * Este teste espera ser retornado o 404 quando o type do produto for inválido.
     */
    @Test
    public void shouldReturn404whenProductsByTypeIsInvalid() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(mockProduct);
        products.add(mockProduct2);
        products.add(mockProduct3);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/fresh-products/list?querytype=ANY"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
