package br.com.meli.PIFrescos.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emails")
public class Email {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer emailId;
  private String ownerReference;
  private String emailFrom;
  private String emailTo;
  @Column(columnDefinition = "TEXT")
  private String text;
  private LocalDateTime sendDateEmail;
  private StatusEmail statusEmail;
}
