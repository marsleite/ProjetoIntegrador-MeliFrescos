package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.ProductRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Marcelo Leite
 * Refactor: Ana Preis
 *
 * */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  List<Product> products = new ArrayList<>();
  Product product1 = new Product(1, "Coca-Cola", StorageType.REFRIGERATED, "Refrigerante");
  Product product2 = new Product(2, "Carne", StorageType.FROZEN, "Proteína Animal");
  Product product3 = new Product(3, "Manga", StorageType.REFRIGERATED, "Fruta");
  Product product4 = new Product();
  @BeforeEach
  void setUp() {
    products.add(product1);
    products.add(product2);
    products.add(product3);
  }

  @AfterEach
  void tearDown() {
    products.clear();
  }


  @Test
  @DisplayName("Test return all products")
  void testGetAllProducts() {
    // testar se o método getAllProducts retorna todos os produtos

    Mockito.when(productRepository.findAll()).thenReturn(products);

    Assertions.assertEquals(products, productService.listAllProducts());
  }

  @Test
  @DisplayName("Test product list is empty")
  void testProductListIsEmpty() {
    // testar se retorna a mensagem se a lista de produtos estiver vazia
    String message = "Product list is empty";
    List<Product> emptyList = Collections.emptyList();
    Mockito.when(productRepository.findAll()).thenReturn(emptyList);

    EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> productService.listAllProducts());
    assertThat(exception.getMessage()).isEqualTo(message);
  }

  @Test
  @DisplayName("Test return product by id")
  void testFindProductById() {
    // testar se o método findProductById retorna o produto com o id especificado

    Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(products.get(0)));

    Assertions.assertEquals(products.get(0), productService.findProductById(1));
  }

  @Test
  @DisplayName("Test return productbyid is not found")
  void testFindProductByIdNotFound() {
    // testar quando o metodo findProductById não encontra o produto com o id especificado e retorna uma exceção

    Mockito.when(productRepository.findById(1)).thenReturn(null);

    Assertions.assertThrows(RuntimeException.class, () -> productService.findProductById(1));
  }

  @Test
  @DisplayName("Test create product")
  void testCreateProduct() {
    // testar se o método createProduct cria um produto e retorna o mesmo

    Product product = new Product(1, "Coca-Cola", StorageType.REFRIGERATED, "Refrigerante");

    Mockito.when(productService.createProduct(product)).thenReturn(product);

    Assertions.assertEquals(productRepository.save(product), productService.createProduct(product));
  }

  @Test
  @DisplayName("Test create product already exists return exception")
  void testCreateProductAlreadyExists() {
    // testar se o método createProduct cria um produto e retorna uma exceção
    Mockito.when(productRepository.findByProductName(product1.getProductName()))
            .thenReturn(product1);

    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> productService.createProduct(product1));
    assertTrue(runtimeException.getMessage().contains("Product already exists"));
  }

  @Test
  @DisplayName("Test update product but product not found return exception")
  void testUpdateProductNotFound() {
    // testar se o método updateProduct atualiza um produto e retorna uma exceção

    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> productService.updateProduct(product4));
    assertTrue(runtimeException.getMessage().contains("Product not found"));
  }

  @Test
  @DisplayName("Test update product")
  void testUpdateProduct() {
    // testar se o método updateProduct atualiza um produto e retorna o mesmo

    Product product = new Product(1, "Coca-Cola", StorageType.REFRIGERATED, "Refrigerante");

    Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(products.get(0)));

    Assertions.assertEquals(productRepository.save(product), productService.updateProduct(product));
  }

  @Test
  @DisplayName("Test delete product")
  void testDeleteProduct() {
    Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(products.get(0)));

    assertEquals(null, productService.deleteProduct(product1.getProductId()));
  }

  @Test
  @DisplayName("Test delete product but product not found")
  void testDeleteProductNotFound() {
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> productService.deleteProduct(product4.getProductId()));
    assertTrue(runtimeException.getMessage().contains("Product not found"));
  }

  @Test
  @DisplayName("Test list by type")
  void testListByType() {
    String type = "FF";
    Mockito.when(productRepository.findAll()).thenReturn(products);
    List<Product> listBy = new ArrayList<>();
    listBy.add(products.get(1));
    assertEquals(listBy, productService.listByType(type));
  }

  @Test
  @DisplayName("Test list by type not found")
  void testListByTypeNotFound() {
    String type1 = "FS";
    Mockito.when(productRepository.findAll()).thenReturn(products);

    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> productService.listByType(type1));
    assertTrue(runtimeException.getMessage().contains("No products found"));
  }
}