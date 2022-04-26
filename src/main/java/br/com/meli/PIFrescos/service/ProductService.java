package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author Marcelo Leite/Juliano Alcione de Souza
 * Refactor: Ana Preis
 *
 */
@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public List<Product> listAllProducts() {
    List<Product> productList = productRepository.findAll();
    if(productList.isEmpty()) {
      throw new EntityNotFoundException("Product list is empty");
    }
    return productList;
  }

  // metodo para buscar produto pelo id no banco de dados
  public Product findProductById(Integer id) {
    return productRepository.findById(id).get();
  }

  // Cria um novo produto, mas antes verifica se já existe um produto com o mesmo nome
  public Product createProduct(Product product) {
    if (productRepository.findByProductName(product.getProductName()) != null) {
      throw new RuntimeException("Product already exists");
    }
    return productRepository.save(product);
  }

  // verifica se o produto existe no banco de dados e faz a atualização
  public Product updateProduct(Product product) {
    if (!productRepository.findById(product.getProductId()).isPresent()) {
      throw new EntityNotFoundException("Product not found");
    }
    return productRepository.save(product);
  }

  public void deleteProduct(Integer id){
    Optional<Product> productOptional = productRepository.findById(id);
    if(productOptional.isEmpty()){
      throw new EntityNotFoundException("Product not found");
    }

    productRepository.delete(productOptional.get());
  }
}
