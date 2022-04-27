package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
