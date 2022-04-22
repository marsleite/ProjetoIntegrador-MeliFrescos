package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne
    private Section section;

    @OneToMany(mappedBy = "inboundOrder", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Batch> batchStock;

}
