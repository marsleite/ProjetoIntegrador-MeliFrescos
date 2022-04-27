package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Marcelo Leite / Juliano Alcione de Souza
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  // metodo para buscar produto pelo nome no banco de dados
  Product findByProductName(String name);

  List<Product> existsByProductName(Integer id);
}
