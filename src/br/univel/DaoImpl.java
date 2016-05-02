package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluir(Object k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

}
