package br.com.meli.PIFrescos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Marcelo Leite
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "batch")
public class Batch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer batchNumber;

  private Float currentTemperature;
  private Float minimumTemperature;
  private Integer initialQuantity;
  private Integer currentQuantity;
  private LocalDate manufacturingDate;
  private LocalDate dueDate;

  @ManyToOne
  private InboundOrder inboundOrder;

  @ManyToOne
  private Product product;

}
