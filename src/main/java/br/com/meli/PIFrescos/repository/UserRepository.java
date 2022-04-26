package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
