package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Email;
import br.com.meli.PIFrescos.models.StatusEmail;
import br.com.meli.PIFrescos.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

  @Autowired
  private EmailRepository emailRepository;

  @Autowired
  private JavaMailSender emailSender;

  public Email sendEmail(Email email) {
    email.setSendDateEmail(LocalDateTime.now());
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(email.getEmailFrom());
      message.setTo(email.getEmailTo());
      message.setSubject(email.getSubject());
      message.setText(email.getText());
      emailSender.send(message);

      email.setStatusEmail(StatusEmail.SENT);
    } catch (MailException e) {
      email.setStatusEmail(StatusEmail.ERROR);
    } finally {
      return emailRepository.save(email);
    }
  }
}
