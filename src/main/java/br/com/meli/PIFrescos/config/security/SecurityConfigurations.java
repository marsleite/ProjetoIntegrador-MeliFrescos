package br.com.meli.PIFrescos.config.security;

import br.com.meli.PIFrescos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Juliano Alcione de Souza
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	//Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configuracoes de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/auth").permitAll()
			//InBoundOrderController
			.antMatchers(HttpMethod.POST, "/fresh-products/users").permitAll()
			.antMatchers( "/fresh-products/inboundorder").hasAnyAuthority("SUPERVISOR", "ADMIN")
			.antMatchers( "/fresh-products/inboundorder/*").hasAnyAuthority("SUPERVISOR", "ADMIN")
			//Product
			.antMatchers( "/fresh-products/").hasAnyAuthority("SUPERVISOR", "ADMIN")
			.antMatchers( "/fresh-products/*").hasAnyAuthority("SUPERVISOR", "ADMIN")
			.antMatchers( "/fresh-products/batch/list*").hasAnyAuthority("SUPERVISOR", "ADMIN")
			//User
			.antMatchers(HttpMethod.GET, "/fresh-products/users").hasAnyAuthority("SUPERVISOR", "ADMIN")
			.antMatchers( "/fresh-products/users/*").hasAnyAuthority("SUPERVISOR", "ADMIN")


		.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(new AuthenticationByTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(HttpMethod.POST, "/auth");
//		web.ignoring().antMatchers(HttpMethod.POST, "/fresh-products/users");
//		web.ignoring().antMatchers(HttpMethod.GET, "/fresh-products/users");
	}

	public static void main(String [] args){
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
}







