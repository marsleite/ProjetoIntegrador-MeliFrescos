package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author Ana Preis
 * Criação da entidade
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer orderNumber;
    private LocalDate orderDate;
    private ArrayList<Batch> batchStock;
}
