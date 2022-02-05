package sav.manager.tables;

import java.util.Arrays;

import sav.manager.Table;

public class Log extends Table
{ //POSIBLE FUTURO SINGLETON
	private static final String TB_NAME = "registro";

	private static final String VALUE = "valor";
	private static final String SALE_DATE = "fecha_de_venta";
	private static final String RETAIL_DUE_DATE = "fecha_de_entrega";
	private static final String[] FIELDS = new String[] {
			ID,
			Admins.GUID,
			Catalog.GUID,
			Users.GUID,
			VALUE,
			SALE_DATE,
			RETAIL_DUE_DATE
	};

	public Log() {
		setTableName(TB_NAME);
		setFields(FIELDS);
	}

        @Override
        protected String getFieldsQuery()
	{
	        StringBuilder sb = new StringBuilder();
	        Arrays.stream(FIELDS).forEach(s -> {
                        switch(s){
                                case ID -> sb.append(String.format("%s int AUTO_INCREMENT", s));
                                case Admins.GUID, Catalog.GUID, Users.GUID -> sb.append(String.format(", %s BINARY(16) NOT NULL", s));
                                default -> sb.append(String.format(", %s VARCHAR(30) NOT NULL", s));
                        }
	        });
                
	        return sb.toString();
	}

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s), UNIQUE KEY(%s), UNIQUE KEY(%s)",
			ID, Admins.GUID, Catalog.GUID, Users.GUID); }
}
