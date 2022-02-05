package sav.manager.tables;

import java.util.Arrays;
import java.util.UUID;

import java.util.logging.Logger;

import sav.manager.Table;
import sav.utils.Crypt;

public class Admins extends Table
{
	private static final Logger LOGGER = Logger.getLogger(Admins.class.getName());

	//TABLE LOGIC
	private static final String TB_NAME = "administradores";

	public static final String GUID = "admin_id";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String BALANCE = "balance";
	private static final String[] FIELDS = new String[] {
			ID,
			GUID,
			EMAIL,
			PASSWORD,
			BALANCE
	};

	public Admins()
	{
		setTableName(TB_NAME);
		setFields(FIELDS);
	}

	private boolean addNewAdmin(String email, String password){ return addNewAdmin(email, password, 0);}
	private boolean addNewAdmin(String email, String password, int initialBalance)
	{
                byte[] compressedGUID = Crypt.guidToBin(UUID.randomUUID());
                byte[] hashedPassword = Crypt.hashPassword(password, compressedGUID);

	        Object[] data = new Object[]{
		        compressedGUID,
		        email,
			hashedPassword,
		        initialBalance };

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
                                default -> sb.append(String.format(", %s VARCHAR(30) NOT NULL", s));
                        }
	        });
                
	        return sb.toString();
	}


        @Override
        protected boolean entryExists(String field, String value)
        {
                boolean res = super.entryExists(field, value);

                if (field.equals(ID) && !res){
                       	LOGGER.info("Cannot find default admin will try to make it.");
		        return addNewAdmin("carlos@wposs.com", "123456");
                }

                return res;
        }
 

	@Override
	protected String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, GUID); }
}
