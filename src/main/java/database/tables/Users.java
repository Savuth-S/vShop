package main.java.database.tables;

import java.util.UUID;

import main.java.database.Table;

public class Users extends Table
{ //POSIBLE FUTURO SINGLETON
	private static final String TB_NAME = "usuarios";

	public static final String UUID = "user_id";
	private static final String NAME = "nombre";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String BALANCE = "balance";
	private static final String LIBRARY = "juegos_rentados";
	private static final String LIST_LIBRARY = "lista_juegos_rentados";
	private static final String[] FIELDS = new String[] {
			ID,
			UUID,
			NAME,
			EMAIL,
			PASSWORD,
			BALANCE,
			LIBRARY,
			LIST_LIBRARY
	};

	public Users(){
		setTableName(TB_NAME);
		setFields(FIELDS);
	}

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, UUID); }
}
