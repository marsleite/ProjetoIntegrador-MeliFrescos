package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Marcelo Leite
 */
@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public List<Product> listAllProducts() {
    return productRepository.findAll();
  }

  // metodo para buscar produto pelo id no banco de dados
  public Product findProductById(Integer id) {
    return productRepository.findById(id).get();
  }

  // Cria um novo produto, mas antes verifica se já existe um produto com o mesmo nome
  public Product createProduct(Product product) {
    if (productRepository.findByProductName(product.getProductName())) {
      throw new RuntimeException("Product already exists");
    }
    return productRepository.save(product);
  }

  // verifica se o produto existe no banco de dados e faz a atualização
  public Product updateProduct(Product product) {
    if (!productRepository.findById(product.getProductId()).isPresent()) {
      throw new RuntimeException("Product not found");
    }
    return productRepository.save(product);
  }
}
