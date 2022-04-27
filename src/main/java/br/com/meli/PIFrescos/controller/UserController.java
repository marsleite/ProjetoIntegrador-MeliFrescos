package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.UserDTO;
import br.com.meli.PIFrescos.controller.forms.UserForm;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.repository.ProfileRepository;
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

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserForm userForm){
        User user = this.userService.create(userForm.convertToEntity(profileRepository));
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserForm userForm,
                                          @PathVariable(name = "id") Integer id) {
        User user = this.userService.update(id, userForm.convertToEntity(profileRepository));
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
