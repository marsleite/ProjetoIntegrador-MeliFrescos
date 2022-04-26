package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.UserDTO;
import br.com.meli.PIFrescos.controller.forms.UserForm;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Api de CRUD dos users
 * @author Juliano Alcione de Souza
*/


@RestController
@RequestMapping("/fresh-products/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserForm userForm){
        User user = this.userService.create(userForm.convertToEntity());
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> all(){
        List<User> all = this.userService.listAll();
        return ResponseEntity.ok(UserDTO.convertList(all));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
