package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private Integer id;

  @NotNull(message = "O fullname não pode ser nulo.")
  @Size(min = 5, max =  30, message = "O fullname deve conter entre 5 a 30 caracteres.")
  private String fullname;

  @NotNull(message = "O email não pode ser nulo.")
  @Email(message = "Email deve ser válido")
  private String email;

  @NotNull(message = "O password não pode ser nulo.")
  @Size(min = 6, message = "O password deve conter no mínimo 6 caracteres.")
  private String password;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  @Enumerated(EnumType.STRING)
  private UserRole role;

}
