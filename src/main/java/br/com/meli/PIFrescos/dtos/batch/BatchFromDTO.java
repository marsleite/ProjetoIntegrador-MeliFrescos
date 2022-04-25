package br.com.meli.PIFrescos.dtos.batch;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BatchFromDTO {
    @NotNull
    private Integer batchNumber;

    @NotNull
    private Integer productId;

    @NotNull
    private Float currentTemperature;

    @NotNull
    private Float minimumTemperature;

    @NotNull
    @Min(value = 0)
    private Integer initialQuantity;

    @NotNull
    @Min(value = 0)
    private Integer currentQuantity;

    @NotNull
    private LocalDate manufacturingDate;

    @NotNull
    private LocalDate expiringDate;

    @NotNull
    private LocalDate dueDate;
}
