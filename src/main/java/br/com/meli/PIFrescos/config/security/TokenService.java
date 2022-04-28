package br.com.meli.PIFrescos.config.security;

import br.com.meli.PIFrescos.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

/**
 * @author Juliano Alcione de Souza
 */
@Service
public class TokenService {
	
	@Value("${spring.jwt.expiration}")
	private String expiration;
	
	@Value("${spring.jwt.secret}")
	private String secret;

	public User getUserLogged(){
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal == null || principal.getId() == null){
			throw new EntityNotFoundException("User not logged");
		}
		return principal;
	}

	public String generateToken(Authentication authentication) {
		User logad = (User) authentication.getPrincipal();
		Date today = new Date();
		Date dateExpiration = new Date(today.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("MeliFrescos")
				.setSubject(logad.getId().toString())
				.setIssuedAt(today)
				.setExpiration(dateExpiration)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Integer getIdUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Integer.parseInt(claims.getSubject());
	}

}
