package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;

/**
 * @author Ana Preis
 * Criação da entidade
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer sectionCode;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "StorageType field can't be empty")
    private StorageType storageType;
    @NotNull(message = "maxCapacity field can't be empty")
    @DecimalMax(value = "150", message = "maxCapacity of Section can't be greater than 150.")
    private Integer maxCapacity;
    @NotNull(message = "currentCapacity field can't be empty")
    @DecimalMax(value = "150", message = "currentCapacity of Section can't be greater than 150.")
    private Integer currentCapacity;

    @ManyToOne
    private Warehouse warehouse;
}
