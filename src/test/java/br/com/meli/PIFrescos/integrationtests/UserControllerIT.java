package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.controller.dtos.UserDTO;
import br.com.meli.PIFrescos.controller.forms.UserForm;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Julio César Gama
 */

@ActiveProfiles("tests")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIT {

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    String userLogin = "{"
            + "\"email\": \"meli@gmail.com\", "
            + "\"password\": \"123456\""
            + "}";

    private String accessToken;

    Profile profile= new Profile();
    User mockedUserBUYER = new User();
    User mockedUserSUPERVISOR = new User();
    User mockedUserSELLER = new User();
    User mockedUserADMIN = new User();

    @BeforeEach
    public void setup() throws Exception{

        profile.setId(1L);
        profile.setName("ADMIN");
        mockedUserADMIN.setId(1);
        mockedUserADMIN.setFullname("John Doe");
        mockedUserADMIN.setEmail("john@mercadolivre.com.br");
        mockedUserADMIN.setPassword("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy");
        mockedUserADMIN.setProfiles(List.of(profile));
        mockedUserADMIN.setRole(UserRole.ADMIN);

        mockedUserBUYER.setRole(UserRole.BUYER);
        mockedUserBUYER.setProfiles(List.of(profile));

        mockedUserSUPERVISOR.setRole(UserRole.SUPERVISOR);
        mockedUserSUPERVISOR.setProfiles(List.of(profile));

        mockedUserSELLER.setRole(UserRole.SELLER);
        mockedUserSELLER.setProfiles(List.of(profile));

        this.accessToken = this.userLogin();
    }


    /**
     * This method returned the mock user token.
     * @return String
     * @author Antonio Hugo
     *
     */

    private String userLogin() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(mockedUserADMIN));
        MvcResult result = mockMvc.perform(post("/auth")
                .contentType("application/json").content(userLogin))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<TokenDto> typeReference = new TypeReference<TokenDto>() {};
        TokenDto token = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        return "Bearer " + token.getToken();
    }

    /**
     * Teste de Integração para retornar todos os usuários cadastrados
     * @author Julio César Gama
     */

    @Test
    public void ShouldGetAllUsers() throws Exception{

        List<User> mockedUsers = new ArrayList<User>();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockedUserADMIN));
        mockedUsers.add(mockedUserBUYER);
        mockedUsers.add(mockedUserSUPERVISOR);
        mockedUsers.add(mockedUserSELLER);
        mockedUsers.add(mockedUserADMIN);

        Mockito.when(userRepository.findAll()).thenReturn(mockedUsers);

        mockMvc.perform(get("/fresh-products/users")
                .header("Authorization",  accessToken))
                .andExpect(status().isOk()).andReturn();
    }


    /**
     * Teste de Integração para cadastrar um novo usuário a partir do endpoint
     * @author Julio César Gama
     */
    @Test
    public void ShouldPostAnUser() throws Exception{

        UserDTO userDTO = new UserDTO(1,"John Doe","john@mercadolivre.com.br",
                "$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy", UserRole.ADMIN);

        UserForm form = new UserForm("John Doe", "john@mercadolivre.com.br",
                "$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy", UserRole.ADMIN);
        form.setAddress(new Address());

       String formString = objectMapper.writeValueAsString(form);

        Mockito.when(userRepository.findByEmail("john@mercadolivre.com.br")).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(mockedUserADMIN);

        MvcResult result = mockMvc.perform(post("/fresh-products/users")
                .contentType("application/json").content(formString))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),UserDTO.class);

        assertEquals(userDTO,userResponse);

    }

    /**
     * Teste de Integração para atualizar um usuário existente a partir do endpoint
     * @author Julio César Gama
     */

    @Test
    public void ShouldUpdateAnUser() throws Exception{

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockedUserADMIN));

        mockedUserADMIN.setFullname("Alfonso Silva");


        UserForm form = new UserForm("John Doe", "john@mercadolivre.com.br",
                "$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy", UserRole.ADMIN);
        form.setAddress(new Address());

        UserDTO userDTO = new UserDTO(1,"Alfonso Silva","john@mercadolivre.com.br",
                "$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy", UserRole.ADMIN);

        String formString = objectMapper.writeValueAsString(form);

        Mockito.when(userRepository.save(any())).thenReturn(mockedUserADMIN);

        MvcResult result = mockMvc.perform(put("/fresh-products/users/1")
                .header("Authorization",  accessToken)
                .contentType("application/json").content(formString))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),UserDTO.class);

        assertEquals(userDTO,userResponse);

    }

    /**
     * Teste de Integração para excluir um usuário existente a partir do endpoint
     * @author Julio César Gama
     */

    @Test
    public void ShouldDeleteAnUserById() throws Exception {

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockedUserADMIN));

        mockMvc.perform(delete("/fresh-products/users/1")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
