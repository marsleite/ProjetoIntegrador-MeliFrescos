package br.com.meli.PIFrescos.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Antonio Hugo
 *
 * Refactor:
 * @author Julio César Gama
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionDTO {

    private int statusCode;
    private String message;
    private String path;

}
