package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Marcelo Leite
 * */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;

  @NotNull(message = "O username não pode ser nulo.")
  @Size(min = 5, max =  10, message = "O username deve conter entre 5 a 10 caracteres.")
  private String username;

  @NotNull(message = "O email não pode ser nulo.")
  @Email(message = "O Email deve ser válido")
  private String email;

  @NotNull(message = "O password não pode ser nulo.")
  @Size(min = 6, message = "O password deve conter no mínimo 6 caracteres.")
  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  private Address address;
}
