package main.java.database.tables;

public class Log extends Table
{ //POSIBLE FUTURO SINGLETON
	private static final String TB_NAME = "registro";

	private static final String VALUE = "valor";
	private static final String SALE_DATE = "fecha_de_venta";
	private static final String RETAIL_DUE_DATE = "fecha_de_entrega";
	private static final String[] FIELDS = new String[] {
			ID,
			Admins.UAID,
			Catalog.UGID,
			Users.UUID,
			VALUE,
			SALE_DATE,
			RETAIL_DUE_DATE
	};

	public Log() {
		setTableName(TB_NAME);
		setFields(FIELDS);
	}
}
