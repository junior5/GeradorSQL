package br.univel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.univel.anotacoes.Coluna;
import br.univel.anotacoes.Tabela;
import br.univel.classeabstrata.SqlGen;
import br.univel.enums.EstadoCivil;

public class SqlGenImpl extends SqlGen {

	private Connection con;

	public SqlGenImpl() throws SQLException {

		abrirConexao();

	}

	private void abrirConexao() throws SQLException {
		String url = "jdbc:h2:D:/";
		String user = "admin";
		String pass = "admin";		
		setCon(DriverManager.getConnection(url, user, pass));
	}

	private void fecharConexao() throws SQLException {
		getCon().close();
	}

	@Override
	protected String getCreateTable(Connection con, Object obj) {

		Class<?> cl = obj.getClass();

		try {

			StringBuilder sb = new StringBuilder(); // Construtor de String

			// Declara��o da tabela.
			String nomeTabela;

			if (cl.isAnnotationPresent(Tabela.class)) {

				Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
				nomeTabela = anotacaoTabela.value();

			} else {
				nomeTabela = cl.getSimpleName().toUpperCase();

			}

			sb.append("CREATE TABLE ").append(nomeTabela).append(" (");

			Field[] atributos = cl.getDeclaredFields();

			// Declara��o das colunas
			for (int i = 0; i < atributos.length; i++) {

				Field field = atributos[i];

				String nomeColuna;
				String tipoColuna;

				if (field.isAnnotationPresent(Coluna.class)) {
					Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

					if (anotacaoColuna.nome().isEmpty()) {
						nomeColuna = field.getName().toUpperCase();
					} else {
						nomeColuna = anotacaoColuna.nome();
					}

				} else {
					nomeColuna = field.getName().toUpperCase();
				}

				Class<?> tipoParametro = field.getType();

				if (tipoParametro.equals(String.class)) {
					tipoColuna = "VARCHAR(";

					if (field.isAnnotationPresent(Coluna.class)) {
						Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

						if (anotacaoColuna.tamanho() == 0) {
							tipoColuna += "100)";
						} else {
							tipoColuna += anotacaoColuna.tamanho() + ")";
						}
					}
				} else if (tipoParametro.equals(int.class)) {
					tipoColuna = "INT";
				} else if (tipoParametro.equals(EstadoCivil.class)) {
					tipoColuna = "INT(1)";
				} else {
					tipoColuna = "DESCONHECIDO";
				}

				if (i > 0) {
					sb.append(",");
				}

				sb.append("\n\t").append(nomeColuna).append(' ').append(tipoColuna);
			}

			// Declara��o das chaves prim�rias
			sb.append(",\n\tPRIMARY KEY( ");

			for (int i = 0, achou = 0; i < atributos.length; i++) {

				Field field = atributos[i];

				if (field.isAnnotationPresent(Coluna.class)) {

					Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

					if (anotacaoColuna.pk()) {

						if (achou > 0) {
							sb.append(", ");
						}

						if (anotacaoColuna.nome().isEmpty()) {
							sb.append(field.getName().toUpperCase());
						} else {
							sb.append(anotacaoColuna.nome());
						}

						achou++;
					}

				}
			}

			sb.append(" )\n);");

			return sb.toString();

		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected String getDropTable(Connection con, Object obj) {

		Class<?> cl = obj.getClass();

		try {

			StringBuilder sb = new StringBuilder(); // Construtor de String

			// Declara��o da tabela.
			String nomeTabela;

			if (cl.isAnnotationPresent(Tabela.class)) {

				Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
				nomeTabela = anotacaoTabela.value();

			} else {
				nomeTabela = cl.getSimpleName().toUpperCase();

			}
			sb.append("DROP TABLE ").append(nomeTabela).append(";");

			return sb.toString();

		} catch (SecurityException e) {
			System.out.println("Tabela n�o encontrada!");
			throw new RuntimeException(e);
		}
	}

	@Override
	protected PreparedStatement getSqlInsert(Connection con, Object obj) {

		Class<? extends Object> cl = obj.getClass();

		StringBuilder sb = new StringBuilder();

		// Declara��o da tabela.
		String nomeTabela;

		if (cl.isAnnotationPresent(Tabela.class)) {
			
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			
			nomeTabela = anotacaoTabela.value();
			
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}

		sb.append("INSERT INTO ").append(nomeTabela).append(" (");

		Field[] atributos = cl.getDeclaredFields();

		for (int i = 0; i < atributos.length; i++) {

			Field field = atributos[i];

			String nomeColuna;

			if (field.isAnnotationPresent(Coluna.class)) {
				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.nome().isEmpty()) {
					nomeColuna = field.getName().toUpperCase();
				} else {
					nomeColuna = anotacaoColuna.nome();
				}

			} else {
				nomeColuna = field.getName().toUpperCase();
			}

			if (i > 0) {
				sb.append(", ");
			}

			sb.append(nomeColuna);
		}

		sb.append(") VALUES (");

		for (int i = 0; i < atributos.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append('?');
		}
		sb.append(");");

		String strSql = sb.toString();

		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(strSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return ps;
	}


	@Override
	protected PreparedStatement getSqlSelectAll(Connection con, Object obj) {
		
		Class<? extends Object> cl = obj.getClass();

		StringBuilder sb = new StringBuilder();

		// Declara��o da tabela.
		String nomeTabela;

		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}

		sb.append("SELECT * FROM ").append(nomeTabela).append(";");

		String strSql = sb.toString();

		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(strSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlSelectById(Connection con, Object obj) {
		
		Class<? extends Object> cl = obj.getClass();

		Cliente cliente = (Cliente) obj;

		StringBuilder sb = new StringBuilder();

		String nomeTabela;

		if (cl.isAnnotationPresent(Tabela.class)) {
			
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			
			nomeTabela = anotacaoTabela.value();
			
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}

		sb.append("SELECT * FROM ").append(nomeTabela).append(" WHERE ");

		Field[] atributos = cl.getDeclaredFields();

		for (int i = 0; i < atributos.length; i++) {

			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {

				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.pk()) {

					if (anotacaoColuna.nome().isEmpty()) {
						sb.append(field.getName().toUpperCase()).append(" = ").append("1");
					} else {
						sb.append(anotacaoColuna.nome()).append(" = ").append(cliente.getId());
					}

				}

			}
		}

		String strSql = sb.toString();

		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement(strSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlUpdateById(Connection con, Object obj) {
		Class<? extends Object> cl = obj.getClass();

		Cliente cliente = (Cliente) obj;

		StringBuilder sb = new StringBuilder();

		// Declara��o da tabela.
		String nomeTabela;

		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}

		sb.append("UPDATE ").append(nomeTabela);

		Field[] atributos = cl.getDeclaredFields();

		for (int i = 0, cont = 0; i < atributos.length; i++) {

			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {

				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (!anotacaoColuna.pk()) {

					cont++;

					if (cont == 1)
						sb.append("\nSET ");

					if (anotacaoColuna.nome().isEmpty()) {

						sb.append(field.getName().toUpperCase()).append(" = (?)");

					} else {

						sb.append(anotacaoColuna.nome()).append(" = (?)");

					}


					if (field.getType().equals(String.class)){
						sb.append(", \n");
					} else {
						sb.append("\n");
					}
				}

			}
		}

		sb.append(" WHERE ");

		for (int i = 0; i < atributos.length; i++) {

			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {

				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.pk()) {

					if (anotacaoColuna.nome().isEmpty()) {

						sb.append(field.getName().toUpperCase()).append(" = ").append("(?)");

					} else {

						sb.append(anotacaoColuna.nome()).append(" = ").append("(?)");

					}

				}
			}
		}

		sb.append(";");

		String strSql = sb.toString();

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(strSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	protected PreparedStatement getSqlDeleteById(Connection con, Object obj) {
		Class<? extends Object> cl = obj.getClass();

		Cliente cliente = (Cliente) obj;

		StringBuilder sb = new StringBuilder();

		// Declara��o da tabela.
		String nomeTabela;

		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else {
			nomeTabela = cl.getSimpleName().toUpperCase();
		}

		sb.append("DELETE FROM ").append(nomeTabela);

		Field[] atributos = cl.getDeclaredFields();

		sb.append(" WHERE ");

		for (int i = 0; i < atributos.length; i++) {

			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {

				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.pk()) {

					if (anotacaoColuna.nome().isEmpty()) {

						sb.append(field.getName().toUpperCase()).append(" = ").append("?;");

					} else {

						sb.append(anotacaoColuna.nome()).append(" = ").append("?;");

					}

				}
			}
		}

		String strSql = sb.toString();

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(strSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return ps;

	}

	public Connection getCon() {
		if (con == null){
			try {
				abrirConexao();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

}
