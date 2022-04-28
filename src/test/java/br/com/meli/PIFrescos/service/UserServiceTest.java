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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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

  @Test
  @DisplayName("Test find user by id")
  void testFindUserById() {
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(users.get(0)));

    assertEquals(users.get(0), userService.findUserById(1));
  }

  @Test
  @DisplayName("Test create user successfully")
  void testCreteUser() {
    Mockito.when(userRepository.save(user1)).thenReturn(user1);

    assertEquals(user1, userService.create(user1));
  }

  @Test
  @DisplayName("Test create user failed")
  void testCreteUserFails() {
    Mockito.when(userRepository.findByEmail(user1.getEmail()))
            .thenReturn(user1);

    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.create(user1));
    assertTrue(runtimeException.getMessage().contains("User already exists"));
  }

  @Test
  @DisplayName("Test update user but not found and return exception")
  void testUpdateUserFails() {
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.update(user1));
    assertTrue(runtimeException.getMessage().contains("User not found"));
  }

  @Test
  @DisplayName("Test update successfull")
  void testUpdateSuccess() {
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(users.get(0)));

    userService.update(user1);

    verify(userRepository).save(user1);
  }

  @Test
  @DisplayName("Test update user id")
  void testUpdate() {
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(users.get(0)));

    userService.update(1, user1);

    verify(userRepository).save(user1);
  }

  @Test
  @DisplayName("Test exceptions delete failed")
  void testDeleteUserFails() {
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.delete(user1.getId()));
    assertTrue(runtimeException.getMessage().contains("User not found"));
  }

  @Test
  @DisplayName("Test delete successful")
  void testDeleteUser() {
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.ofNullable(users.get(0)));

    userService.delete(1);

    verify(userRepository).delete(user1);
  }
}
