package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Juliano Alcione de Souza
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
  Optional<Profile> findByName(String role);
}
