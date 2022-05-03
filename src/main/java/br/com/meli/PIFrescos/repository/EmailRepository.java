package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Integer> {
}
