package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.univel.enums.EstadoCivil;


public class Main extends DaoImpl {	
	
	public Main() {	
		
		Cliente c1 = new Cliente(1, "Junior", "Rua: Carijós", "9988-6760", EstadoCivil.SOLTEIRO);

		cabecalho("APAGANDO TABELA:");
		apagarTabela(c1);

		criarTabela(c1);

		salvar(c1);

		Cliente c2 = new Cliente(2, "Lucas", "Rua: Xavantes", "3226-2892", EstadoCivil.CASADO);

		salvar(c2);

		Cliente c3 = new Cliente(3, "Teste", "Avenida Brasil", "0000000", EstadoCivil.VIUVO);

		salvar(c3);

		listarTodos(new Cliente());

		buscar(c1);

		c3.setNome("NOME ATUALIZADO");
		c3.setEndereco("ENDERECO ATUALIZADO");
		c3.setTelefone("TELEFONE ATUALIZADO");
		c3.setEstadoCivil(EstadoCivil.SOLTEIRO);

		atualizar(c3);

		excluir(c2);

		listarTodos(new Cliente());	
		
	}
	
	private void cabecalho(String string) {
		System.out.println("\n----------------------------------------------");
		System.out.println(string);
		System.out.println("----------------------------------------------");
	}

	
	public static void main(String[] args) {
		new  Main();
	}	
}

