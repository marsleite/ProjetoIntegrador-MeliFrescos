package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.controller.dtos.EmailDTO;
import br.com.meli.PIFrescos.models.Email;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.UserRepository;
import org.springframework.beans.BeanUtils;
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
  private EmailService emailService;

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

    User createdUser = userRepository.save(user);
    EmailDTO emailDTO = new EmailDTO();
    emailDTO.setOwnerReference(createdUser.getId());
    emailDTO.setEmailFrom("no-replay@fresh.com");
    emailDTO.setEmailTo(createdUser.getEmail());
    emailDTO.setSubject("Bem-vindo(a) à sua conta PI group3 Fresh");
    emailDTO.setText("Olá, " + createdUser.getFullname() + "!\n"
            + "Seu cadastro foi concluído com sucesso e agora você pode aproveitar ao máximo as ofertas do nosso site!");

    Email email = new Email();
    BeanUtils.copyProperties(emailDTO, email);
    emailService.sendEmail(email);
    return createdUser;
  }

  // verifica se o usuario existe no banco de dados e faz a atualização
  public User update(User user) {
    if (!userRepository.findById(user.getId()).isPresent()) {
      throw new EntityNotFoundException("User not found");
    }
    return userRepository.save(user);
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
  }
}
