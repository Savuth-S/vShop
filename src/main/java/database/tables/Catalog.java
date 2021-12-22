package main.java.database.tables;

import java.sql.*;
import java.util.Arrays;

public class Catalog implements Table{ //POSIBLE FUTURO SINGLETON
	public static final String TB_NAME = "catalogo";
	private static final String ID = "id";
	private static final String UUID = "user_unique_id";
	private static final String NAME = "nombre";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String BALANCE = "balance";
	
	private static final String[] fields = new String[] {
			ID,
			UUID,
			NAME,
			EMAIL,
			PASSWORD,
			BALANCE
	};

	public void create(Connection connection){
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", TB_NAME, getFieldsQuery());

		try (Statement cmnd = connection.createStatement()){
			cmnd.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getFieldsQuery() {
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(s -> {
			if (s.equals(ID)){
				sb.append(String.format("%1$s int NOT NULL AUTO_INCREMENT, PRIMARY KEY (%1$s)", s));
			}else{
				sb.append(String.format(", %s VARCHAR(30) NOT NULL", s));
			}
		});

		return sb.toString();
	}
}
