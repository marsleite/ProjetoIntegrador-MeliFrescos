package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Address;
import br.com.meli.PIFrescos.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * @author Marcelo Leite
 */
@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  public Address findAddressById(Integer id) {
    return addressRepository.findById(id).get();
  }

  public Address createAddress(Address address) {
    return addressRepository.save(address);
  }

  public Address updateAddress(Address address) {
    if (!addressRepository.findById(address.getId()).isPresent()) {
      throw new EntityNotFoundException("Address not found");
    }
    return addressRepository.save(address);
  }

  public void deleteAddress(Integer id){
    Optional<Address> addressOptional = addressRepository.findById(id);
    if(addressOptional.isEmpty()){
      throw new EntityNotFoundException("Address not found");
    }

    addressRepository.delete(addressOptional.get());
  }

}
