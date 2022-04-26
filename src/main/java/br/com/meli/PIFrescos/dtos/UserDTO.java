package br.com.meli.PIFrescos.dtos;

import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Address;

/**
 * @author Marcelo Leite
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Integer userId;
  private String username;
  private String email;
  private UserRole role;
  private Address address;

  public UserDTO(User user) {
    this.userId = user.getUserId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.address = user.getAddress();
  }
}
