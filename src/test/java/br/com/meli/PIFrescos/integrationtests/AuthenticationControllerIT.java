package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.models.Profile;
import br.com.meli.PIFrescos.models.User;
import br.com.meli.PIFrescos.models.UserRole;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Antonio Hugo
 */

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerIT {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    String userLoginPayload = "{"
            + "\"email\": \"meli@gmail.com\","
            + "\"password\": \"123456\""
            + "}";

    User userMock = new User();
    Profile profile = new Profile();

    @BeforeEach
    public void setUp() throws Exception {
        profile.setId(1L);
        profile.setName("ADMIN");
        userMock.setId(1);
        userMock.setFullname("John Doe");
        userMock.setEmail("john@mercadolivre.com.br");
        userMock.setPassword("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy");
        userMock.setProfiles(List.of(profile));
        userMock.setRole(UserRole.ADMIN);
    }

    /**
     * Este teste deve fazer autenticação do usuario e retornar o token
     * @author Antonio Hugo
     */
    @Test
    public void shouldAuthenticateWithLoginPasswordAndReturnToken() throws Exception {

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));

        MvcResult result = mockMvc.perform(post("/auth")
                .contentType("application/json").content(userLoginPayload))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<TokenDto> typeReference = new TypeReference<TokenDto>() {};
        TokenDto token = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertThat(token).isNotNull();
        assertThat(token.getTipo()).isEqualTo("Bearer");
    }

    /**
     * Este teste deve retornar 400 quando email for inválido
     * @author Antonio Hugo
     */
    @Test
    public void shouldAuthenticateWithInvalidEmailReturn400() throws Exception {
        String invalidPayload = "{"
                + "\"password\": \"123456\""
                + "}";

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));

        mockMvc.perform(post("/auth")
                .contentType("application/json").content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].field").value("email"))
                .andExpect(jsonPath("$[*].error").value("must not be null"))
                .andReturn();

    }
    /**
     * Este teste deve retornar 400 quando password for inválido
     * @author Antonio Hugo
     */

    @Test
    public void shouldAuthenticateWithInvalidPasswordReturn400() throws Exception {
        String invalidPayload = "{"
                + "\"email\": \"meli@mercadolivre.com\""
                + "}";

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));

        mockMvc.perform(post("/auth")
                .contentType("application/json").content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].field").value("password"))
                .andExpect(jsonPath("$[*].error").value("must not be null"))
                .andReturn();

    }
}
