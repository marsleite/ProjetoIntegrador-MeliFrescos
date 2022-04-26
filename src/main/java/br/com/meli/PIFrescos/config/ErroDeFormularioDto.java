package br.com.meli.PIFrescos.config;

/**
 * Classe ErroDeFormularioDto para formatar mensagem de erro
 * @author Juliano Alcione de Souza
 */
public class ErroDeFormularioDto {
	
	private String campo;
	private String erro;
	
	public ErroDeFormularioDto(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
	
	

}
