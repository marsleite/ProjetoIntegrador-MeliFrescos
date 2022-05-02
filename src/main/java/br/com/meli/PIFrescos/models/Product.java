package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer productId;
  private String productName;
  @Enumerated(EnumType.STRING)
  private StorageType productType;
  private String productDescription;
}