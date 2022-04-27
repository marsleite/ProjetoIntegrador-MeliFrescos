package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Juliano Alcione de Souza
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByEmail(String username);
}
