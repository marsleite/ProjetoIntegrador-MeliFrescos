package br.com.meli.PIFrescos.config;

/**
 * Classe ErrorFormsDto para formatar mensagem de erro
 * @author Juliano Alcione de Souza
 */
public class ErrorFormsDto {
	
	private String field;
	private String error;
	
	public ErrorFormsDto(String field, String error) {
		this.field = field;
		this.error = error;
	}

	public String getField() {
		return field;
	}

	public String getError() {
		return error;
	}

}
