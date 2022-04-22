package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private StorageType storageType;
    private Integer maxCapacity;
    private Integer currentCapacity;
}
