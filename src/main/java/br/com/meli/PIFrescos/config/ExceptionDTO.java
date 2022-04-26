package br.com.meli.PIFrescos.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Antonio Hugo
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int statusCode;
    private String trace;
    private String message;
    private String path;

}
