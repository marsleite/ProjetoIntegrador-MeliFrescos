package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Marcelo Leite
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

  private String productName;
  @Enumerated(EnumType.STRING)
  private StorageType productType;
  private String productDescription;
}