package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "O nome da rua não pode ser nulo.")
  private String street;
  private String number;

  @NotNull(message = "O nome da região não pode ser nulo.")
  private String region;

  @NotNull(message = "O CEP não pode ser nulo.")
  private String zipcode;

  @OneToOne(mappedBy = "address")
  private User user;

}
