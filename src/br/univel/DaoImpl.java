package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import br.univel.interfaces.Dao;

public class DaoImpl implements Dao<Object, Object> {

	private Connection con;
	
	private Connection abrirConexao() throws SQLException{
		String url = "jdbc:h2:~/pessoa";
		String user = "sa";
		String pass = "sa";
		return con = DriverManager.getConnection(url, user, pass);

	}
	
	@Override
	public void salvar(Object t) {
		try {
			con = abrirConexao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SqlGenImpl sqlGenEx = new SqlGenImpl();
		sqlGenEx.getSqlInsert(con, t);		
	}
	

	@Override
	public Object buscar(Object k) {
		// TODO Auto-generated method stub
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
