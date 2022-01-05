package main.java.database.tables;

import java.util.UUID;

import java.util.logging.Logger;

import main.java.database.Database;
import main.java.database.Table;
import main.java.utils.Utils;

public class Admins extends Table
{
	private static final Logger LOGGER = Logger.getLogger(Admins.class.getName());

	//TABLE LOGIC
	private static final String TB_NAME = "administradores";

	public static final String GUID = "guid";
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
		try (Database db = Database.getInstance()) {
                        byte[] compressedGUID = Utils.guidToBin(UUID.randomUUID());
		        Object[] data = new Object[]{
			        compressedGUID,
			        email,
				password,
			        initialBalance,
                        };

			return insert(FIELDS, data) != null;
		}
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
