package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Address;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import br.com.meli.PIFrescos.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marcelo Leite
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  List<User> users = new ArrayList<>();
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

    users.add(user1);
  }

  @Test
  @DisplayName("Test find all users")
  void testFindaAllUsers() {
    Mockito.when(userRepository.findAll()).thenReturn(users);

    assertEquals(users, userService.listAll());
  }
}
