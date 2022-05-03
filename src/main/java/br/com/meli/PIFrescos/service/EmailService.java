package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Email;
import br.com.meli.PIFrescos.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private EmailRepository emailRepository;

  public void sendEmail(Email email) {

  }
}
