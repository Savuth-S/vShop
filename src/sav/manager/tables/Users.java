package sav.manager.tables;

import java.util.Arrays;
import java.util.UUID;

import sav.manager.Table;
import sav.utils.Crypt;
import sav.vshop.database.DbHelper;

public class Users extends Table
{ //POSIBLE FUTURO SINGLETON
	private static final String TB_NAME = "usuarios";

	public static final String GUID = "user_id";
	private static final String NAME = "nombre";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String BALANCE = "balance";
	private static final String LIBRARY = "juegos_rentados";
	private static final String LIST_LIBRARY = "lista_juegos_rentados";
	private static final String[] FIELDS = new String[] {
			ID,
			GUID,
			NAME,
			EMAIL,
			PASSWORD,
			BALANCE,
			LIBRARY,
			LIST_LIBRARY
	};

	public Users(DbHelper db){ super(db, TB_NAME, FIELDS); }

        private boolean addNewUser(String email, String password)
	{
                byte[] compressedGUID = Crypt.uuidToBin(UUID.randomUUID());
                byte[] hashedPassword = Crypt.hashPassword(password, compressedGUID);
                
	        Object[] data = new Object[]{
		        compressedGUID,
		        email,
			hashedPassword,
		        0 };

		return insert(FIELDS, data) != null;
	}

        @Override
        protected String getFieldsQuery()
	{
	        StringBuilder sb = new StringBuilder();
	        Arrays.stream(FIELDS).forEach(s -> {
                        switch(s){
                                case ID -> sb.append(String.format("%s int AUTO_INCREMENT", s));
                                case GUID -> sb.append(String.format(", %s BINARY(16) NOT NULL", s));
                                case PASSWORD -> sb.append(String.format(", %s BINARY(48) NOT NULL", s));
                                default -> sb.append(String.format(", %s VARCHAR(30)", s));
                        }
	        });
                
	        return sb.toString();
	}

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, GUID); }
}
