package br.com.meli.PIFrescos.controller.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailDTO {

  @NotBlank
  private String ownerReference;
  @NotBlank
  @Email
  private String emailFrom;
  @NotBlank
  @Email
  private String emailTo;
  @NotBlank
  private String Subject;
  @NotBlank
  private String text;
}
