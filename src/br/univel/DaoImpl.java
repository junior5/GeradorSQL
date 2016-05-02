package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.univel.enums.EstadoCivil;
import br.univel.interfaces.Dao;

public class DaoImpl implements Dao<Object, Object> {
	
	private SqlGenImpl impl;
	
	private SqlGenImpl getImp() {
		if (impl == null) {
			try {
				setImp(new SqlGenImpl());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return impl;
	}
	
	private void setImp(SqlGenImpl impl) {
		this.impl = impl;
	}
	
	@Override
	public void salvar(Object t) {

		PreparedStatement insert = getImp().getSqlInsert(getImp().getCon(), t);

		System.out.println("INCLUINDO REGISTRO");

		Cliente cliente = (Cliente) t;

		try {
			insert.setInt(1, cliente.getId());
			insert.setString(2, cliente.getNome());
			insert.setString(3, cliente.getEndereco());
			insert.setString(4, cliente.getTelefone());
			insert.setInt(5, cliente.getEstadoCivil().ordinal());
		} catch (SQLException e) {
			e.printStackTrace();
		}	

		try {
			insert.executeUpdate();
			System.out.println("REGISTRO INCLUIDO!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	@Override
	public Object buscar(Object k) {
		
		PreparedStatement buscar = impl.getSqlSelectById(impl.getCon(), k);

		ResultSet exibir;

		System.out.println("BUSCANDO REGISTRO");

		try {
			exibir = buscar.executeQuery();
			while (exibir.next()) {
				System.out.print("\nID: " + exibir.getInt("CL_ID"));
				System.out.print("\nNOME: " + exibir.getString("CL_NOME"));
				System.out.print("\nENDERECO: " + exibir.getString("CL_ENDERECO"));
				System.out.print("\nTELEFONE: " + exibir.getString("CL_TELEFONE"));
				System.out.print("\nESTADOCIVIL: " + EstadoCivil.values()[exibir.getInt("CL_ESTADOCIVIL")]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void atualizar(Object t) {
		
		PreparedStatement alterar = impl.getSqlUpdateById(impl.getCon(), t);

		Cliente cliente = (Cliente) t;

		int exibir = 0;

		System.out.println("ATUALIZANDO REGISTRO ID: " + cliente.getId());

		try {
			alterar.setString(1, cliente.getNome());
			alterar.setString(2, cliente.getEndereco());
			alterar.setString(3, cliente.getTelefone());
			alterar.setInt(4, cliente.getEstadoCivil().ordinal());
			alterar.setInt(5, cliente.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			exibir = alterar.executeUpdate();
			System.out.print("\nNovo nome: " + cliente.getNome());
			System.out.print("\nNovo endereço: " + cliente.getEndereco());
			System.out.print("\nNovo telefone : " + cliente.getTelefone());
			System.out.print("\nNovo estado civil : " + cliente.getEstadoCivil());
			System.out.print("\n " + exibir + " Registro(s) alterados!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void excluir(Object k) {
		PreparedStatement excluir = impl.getSqlDeleteById(impl.getCon(), k);

		Cliente cliente = (Cliente) k;

		int exibir = 0;

		System.out.println("EXCLUINDO REGISTRO ID: " + cliente.getId());

		try {
			excluir.setInt(1, cliente.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			exibir = excluir.executeUpdate();
			System.out.println(exibir + " Registro(s) excluido(s)!");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<Cliente> listarTodos(Object t) {

		PreparedStatement listar = impl.getSqlSelectAll(impl.getCon(), t);

		System.out.println("LISTANDO REGISTROS");

		List<Cliente> clientes = new ArrayList<Cliente>();

		ResultSet retorno = null;

		try {
			retorno = listar.executeQuery();
			while (retorno.next()) {
				Cliente aux = new Cliente();
				aux.setId(retorno.getInt("CL_ID"));
				aux.setNome(retorno.getString("CL_NOME"));
				aux.setEndereco(retorno.getString("CL_ENDERECO"));
				aux.setTelefone(retorno.getString("CL_TELEFONE"));
				aux.setEstadoCivil(EstadoCivil.values()[retorno.getInt("CL_ESTADOCIVIL")]);

				clientes.add(aux);

				}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		for (Cliente c: clientes){
			System.out.println("ID: " + c.getId());
			System.out.println("NOME: " + c.getNome());
			System.out.println("ENDERECO: " + c.getEndereco());
			System.out.println("TELEFONE: " + c.getTelefone());
			System.out.println("ESTADO CIVIL: " + c.getEstadoCivil());
			System.out.println("\n");
		}
		return clientes;
	}
	
	public void apagarTabela(Object obj){
		String sql = getImp().getDropTable(getImp().getCon(), obj);

		System.out.println("APAGANDO TABELA");

		System.out.println(sql);
		try (PreparedStatement ps = getImp().getCon().prepareStatement(sql)){
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("TABELA NÃO ENCONTRADA!");
		}
	}
	
	public void criarTabela(Object obj){
		String sql = getImp().getCreateTable(getImp().getCon(), obj);

		System.out.println("CRIANDO TABELA");

		System.out.println(sql);

		try (PreparedStatement ps = getImp().getCon().prepareStatement(sql)){
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
