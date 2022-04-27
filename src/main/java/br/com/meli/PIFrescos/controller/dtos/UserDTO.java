package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marcelo Leite
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Integer userId;
  private String fullname;
  private String email;
  private String password;
  private String address;
  private UserRole role;

  public UserDTO(User user) {
    this.userId = user.getId();
    this.fullname = user.getFullname();
    this.password = user.getPassword();
    this.email = user.getEmail();
    this.role = user.getRole();
  }

  public static List<UserDTO> convertList(List<User> users){
    return users.stream().map(UserDTO::new).collect(Collectors.toList());
  }
}
