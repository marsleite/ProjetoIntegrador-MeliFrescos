package br.com.meli.PIFrescos.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "msg_emails")
public class Email {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer emailId;
  private String emailTo;
  @Enumerated(EnumType.STRING)
  private StatusEmail reference;
  @Enumerated(EnumType.STRING)
  private StatusEmail statusEmail;
  private LocalDateTime sendDateEmail;
}
