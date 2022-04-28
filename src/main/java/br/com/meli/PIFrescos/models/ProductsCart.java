package br.com.meli.PIFrescos.models;

import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;

/**
 * @author Ana Preis
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Batch field can't be empty")
    @ManyToOne(fetch = FetchType.EAGER)
    private Batch batch;
    @NotNull(message = "Quantity field can't be empty")
    @DecimalMax(value = "200", message = "Quantity of ProductsCart can't be greater than 200.")
    private Integer quantity;
}