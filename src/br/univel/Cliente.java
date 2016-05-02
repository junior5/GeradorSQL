package br.univel;

import br.univel.anotacoes.Coluna;
import br.univel.anotacoes.Tabela;
import br.univel.enums.EstadoCivil;

@Tabela("CAD_CLIENTE")
public class Cliente {
	
	@Coluna(pk=true, nome="ID", tamanho=6)
	private int id;
	
	@Coluna(nome="CL_NOME", tamanho=50)
	private String nome;
	
	@Coluna(nome="CL_ENDERECO", tamanho=80)
	private String endereco;
	
	@Coluna(nome="CL_TELEFONE", tamanho=10)
	private String telefone;
	
	@Coluna(nome="CL_ESTADOCIVIL", tamanho=30)
	private EstadoCivil estadoCivil;
	
	public Cliente() {
		this(0, null, null, null, null);
	}

	public Cliente(int id, String nome, String endereco, String telefone, EstadoCivil estadoCivil) {
		super();
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.telefone = telefone;
		this.estadoCivil = estadoCivil;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}	

}
