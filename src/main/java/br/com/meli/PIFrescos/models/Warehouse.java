package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author Ana Preis
 * Criação da entidade
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warehouseCode;
    // private String representante;

    @OneToMany
    private List<Section> sectionList;
}
