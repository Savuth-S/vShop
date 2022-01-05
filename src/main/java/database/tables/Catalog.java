package main.java.database.tables;

import main.java.database.Table;

public class Catalog extends Table
{ //POSIBLE FUTURO SINGLETON
	public static final String TB_NAME = "catalogo";

	public static final String UGID = "game_id";
	private static final String NAME = "nombre";
	private static final String RELEASE = "fecha_de_salida";
	private static final String PLATFORM = "plataforma";
	private static final String COMPANY = "productora";
	private static final String DIRECTOR = "director";
	private static final String MAIN_CHAR = "protagonista";
	private static final String[] FIELDS = new String[] {
			ID,
			UGID,
			NAME,
			RELEASE,
			PLATFORM,
			COMPANY,
			DIRECTOR,
			MAIN_CHAR
	};

	public Catalog(){
		setTableName(TB_NAME);
		setFields(FIELDS);
	}

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, UGID); }
}
