package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Address;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import br.com.meli.PIFrescos.repository.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Marcelo Leite
 */
@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private AddressService addressService;

  List<Address> addresses = new ArrayList<>();
  User user1 = new User();
  Address address1 = new Address();
  @BeforeEach
  void setup() {
    user1.setId(1);
    user1.setFullname("Marcelo");
    user1.setEmail("marcelo@email.com");
    user1.setPassword("123456");
    user1.setRole(UserRole.BUYER);
    user1.setAddress(address1);

    address1.setId(1);
    address1.setStreet("Rua");
    address1.setNumber("90");
    address1.setRegion("sp");
    address1.setZipcode("12356-166");

    addresses.add(address1);

  }

  @Test
  @DisplayName("Return address by id")
  void testFindAddressById() {
    Mockito.when(addressRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(addresses.get(0)));

    Assertions.assertEquals(addresses.get(0), addressService.findAddressById(1));
  }

  @Test
  @DisplayName("Test create address")
  void testeCreateAddress() {
    Mockito.when(addressRepository.save(address1)).thenReturn(address1);

    Assertions.assertEquals(addressRepository.save(address1), addressService.createAddress(address1));
  }

  @Test
  @DisplayName("Test update address")
  void  testUpdateAddress() {
    Mockito.when(addressRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(addresses.get(0)));

    Assertions.assertEquals(addressRepository.save(address1), addressService.updateAddress(address1));
  }

  @Test
  @DisplayName("Test exceptions update address")
  void testExceptionsUpdateAddress() {
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> addressService.updateAddress(address1));
    assertTrue(runtimeException.getMessage().contains("Address not found"));
  }

  @Test
  @DisplayName("Test exceptions delete address")
  void testExceptionsDeleteAddress() {
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> addressService.deleteAddress(address1.getId()));
    assertTrue(runtimeException.getMessage().contains("Address not found"));
  }

  @Test
  @DisplayName("Test delete address successful")
  void testDeleteAddressSuccess() {
    Mockito.when(addressRepository.findById(address1.getId())).thenReturn(Optional.ofNullable(address1));

    addressService.deleteAddress(1);

    verify(addressRepository).delete(any());
  }
}
