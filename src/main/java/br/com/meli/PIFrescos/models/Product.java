package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * @author Marcelo Leite
 *
 * Validações
 * @author Julio César Gama
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer productId;

  @NotNull(message = "O nome do produto não pode ser nulo.")
  @Size(min = 5, max =  255, message = "O nome do produto deve conter entre 5 a 255 caracteres.")
  private String productName;

  @Enumerated(EnumType.STRING)
  private StorageType productType;

  @NotNull (message = "A descrição do produto não pode ser nulo.")
  @Size(min = 5, max =  255, message = "A descrição do produto deve conter entre 5 a 255 caracteres.")
  private String productDescription;
}