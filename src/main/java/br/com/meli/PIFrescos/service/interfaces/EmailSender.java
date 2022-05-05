package br.com.meli.PIFrescos.service.interfaces;

/**
 * @author Marcelo Leite
 * Requisito 6
 */
public interface EmailSender {
  void sendEmail(String to, String email);
}
