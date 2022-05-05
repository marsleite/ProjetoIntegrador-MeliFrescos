package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.email.EmailTemplate;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.UserRepository;
import br.com.meli.PIFrescos.service.interfaces.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author Juliano Alcione de Souza
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailSender emailSender;

  public List<User> listAll() {
    return userRepository.findAll();
  }

  // metodo para buscar usuario pelo id no banco de dados
  public User findUserById(Integer id) {
    return userRepository.findById(id).get();
  }

  // Cria um novo usuario, mas antes verifica se já existe um com o mesmo nome
  public User create(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new RuntimeException("User already exists");
    }

    /**
     * @author Marcelo Leite
     * Requisito 6
     */
    String link = "http://localhost:8080/";
    String title = "Confirmação de registro";
    User createdUser = userRepository.save(user);

    emailSender.sendEmail(createdUser.getEmail(),
            EmailTemplate.confirmEmail(createdUser.getFullname(), link, title));
    return createdUser;
  }

  // verifica se o usuario existe no banco de dados e faz a atualização
  public User update(User user) {
    if (!userRepository.findById(user.getId()).isPresent()) {
      throw new EntityNotFoundException("User not found");
    }
    /**
     * @author Marcelo Leite
     * Requisito 6
     */
    String link = "http://localhost:8080/";
    String title = "Confirmação de alteração";
    User updatedUser = userRepository.save(user);

    emailSender.sendEmail(updatedUser.getEmail(),
            EmailTemplate.confirmUpdateEmail(updatedUser.getFullname(), link, title));

    return updatedUser;
  }

  public User update(Integer id, User user) {
    user.setId(id);
    return update(user);
  }

  public void delete(Integer id){
    Optional<User> userOptional = userRepository.findById(id);
    if(userOptional.isEmpty()){
      throw new EntityNotFoundException("User not found");
    }

    userRepository.delete(userOptional.get());
    /**
     * @author Marcelo Leite
     * Requisito 6
     */
    String link = "http://localhost:8080/";
    String title = "Confirmação de exclusão de cadastro";

    emailSender.sendEmail(userOptional.get().getEmail(),
            EmailTemplate.confirmDeleteEmail(userOptional.get().getFullname(), link, title));

  }

}
