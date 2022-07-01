package sav.manager.tables;

import java.util.Arrays;

import sav.manager.Table;
import sav.vshop.database.DbHelper;

public class Catalog extends Table
{ //POSIBLE FUTURO SINGLETON
	public static final String TB_NAME = "catalogo";

	public static final String GUID = "catalog_id";
	private static final String NAME = "nombre";
	private static final String RELEASE = "fecha_de_salida";
	private static final String PLATFORM = "plataforma";
	private static final String COMPANY = "productora";
	private static final String DIRECTOR = "director";
	private static final String MAIN_CHAR = "protagonista";
	private static final String[] FIELDS = new String[] { 
		ID, 
		GUID, 
		NAME,
		RELEASE, 
		PLATFORM, 
		COMPANY, 
		DIRECTOR, 
		MAIN_CHAR };

	public Catalog(DbHelper db){ super(db, TB_NAME, FIELDS); }

	@Override
	protected String getFieldsQuery()
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(FIELDS).forEach(s -> {
			switch(s){
				case ID -> sb.append(String.format("%s int AUTO_INCREMENT", s));
				case GUID -> sb.append(String.format(", %s BINARY(16) NOT NULL", s));
				default -> sb.append(String.format(", %s VARCHAR(30) NOT NULL", s)); }
		});

		return sb.toString();
	}

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, GUID); }
}
