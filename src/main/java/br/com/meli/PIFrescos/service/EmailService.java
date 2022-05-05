package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Email;
import br.com.meli.PIFrescos.models.StatusEmail;
import br.com.meli.PIFrescos.repository.EmailRepository;
import br.com.meli.PIFrescos.service.interfaces.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

/**
 * @author Marcelo Leite
 * Requisito 6
 */
@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

  private final JavaMailSender mailSender;

  @Autowired
  private EmailRepository emailRepository;

  @Override
  @Async
  public void sendEmail(String to, String email, Email emailModel) {
    emailModel.setSendDateEmail(LocalDateTime.now());
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(email, true);
      helper.setTo(to);
      helper.setSubject("Confirmação!");
      helper.setFrom("no-reply@gmail.com");
      mailSender.send(mimeMessage);

      emailModel.setStatusEmail(StatusEmail.SENT);
    } catch(MessagingException e) {
      emailModel.setStatusEmail(StatusEmail.ERROR);
      throw new IllegalStateException("failed to send email");
    } finally {
      emailRepository.save(emailModel);
    }
  }
}