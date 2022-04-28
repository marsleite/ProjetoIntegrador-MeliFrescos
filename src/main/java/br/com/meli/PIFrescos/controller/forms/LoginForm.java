package br.com.meli.PIFrescos.controller.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotNull;

/**
 * @author Juliano Alcione de Souza
 */
@Getter
@Setter
public class LoginForm {

	@NotNull
	private String email;
	@NotNull
	private String password;

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

}
