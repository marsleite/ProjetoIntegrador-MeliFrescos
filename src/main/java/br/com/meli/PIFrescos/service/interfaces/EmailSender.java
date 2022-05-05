package br.com.meli.PIFrescos.service.interfaces;

import br.com.meli.PIFrescos.models.Email;

/**
 * @author Marcelo Leite
 * Requisito 6
 */
public interface EmailSender {
  void sendEmail(String to, String email, Email emailModel);
}
