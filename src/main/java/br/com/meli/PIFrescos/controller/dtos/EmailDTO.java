package br.com.meli.PIFrescos.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {


  private Integer ownerReference;

  private String emailFrom;

  private String emailTo;

  private String subject;

  private String text;

}
