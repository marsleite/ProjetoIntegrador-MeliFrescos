package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Profile;
import br.com.meli.PIFrescos.models.Address;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
import br.com.meli.PIFrescos.repository.ProfileRepository;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Juliano Alcione de Souza
 * */

@Getter
@Setter
public class UserForm {
  @NotNull(message = "O fullname não pode ser nulo.")
  @Size(min = 5, max =  30, message = "O fullname deve conter entre 5 a 30 caracteres.")
  private String fullname;

  @NotNull(message = "O email não pode ser nulo.")
  @Email(message = "Email deve ser válido")
  private String email;

  @NotNull(message = "O password não pode ser nulo.")
  @Size(min = 6, message = "O password deve conter no mínimo 6 caracteres.")
  private String password;

  @NotNull(message = "O address não pode ser nulo.")
  private Address address;

  @NotNull(message = "O role não pode ser nulo.")
  private UserRole role;

  private List<Profile> perfis = new ArrayList<>();


  public UserForm(String fullname, String email, String password, UserRole role) {
    this.fullname = fullname;
    this.email = email;
    this.password = new BCryptPasswordEncoder().encode(password);
    this.role = role;
  }

  public User convertToEntity(ProfileRepository profileRepository){
    Optional<Profile> profile = profileRepository.findByName(role.toString());
    if(profile.isEmpty()){
      throw new RuntimeException("Profile not found");
    }

    return new User(null, fullname, email, password, role, address, List.of(profile.get()));
  }
}
