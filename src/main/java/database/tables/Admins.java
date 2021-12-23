package main.java.database.tables;

import com.google.common.hash.Hashing;
import main.java.database.Database;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Admins extends Table
{
	private static final Logger LOGGER = Logger.getLogger(Admins.class.getName());

	//TABLE LOGIC
	private static final String TB_NAME = "administradores";

	public static final String UAID = "admin_id";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String BALANCE = "balance";
	private static final String DATE = "fecha";
	private static final String[] FIELDS = new String[] {
			ID,
			UAID,
			EMAIL,
			PASSWORD,
			BALANCE,
			DATE
	};

	public Admins()
	{
		setTableName(TB_NAME);
		setFields(FIELDS);
	}

	@Override
	String getUniques() { return String.format(", PRIMARY KEY (%s), UNIQUE KEY (%s)", ID, UAID); }

	public static boolean makeDefaultAdmin()
	{
		try (Database db = Database.getInstance()) {
			ResultSet res = db.execute(String.format("SELECT * FROM %s WHERE %s = 1 LIMIT 1", TB_NAME, ID));
			if (!res.first()) {
				LOGGER.info("Cannot find default admin will try to make it.");
				return addNewAdmin("carlos@wposs.com", "123456");
			} else {
				return true;
			}
		}catch (SQLException e) {
			LOGGER.warning(String.format("FAILED TO MAKE ADMIN %s", e.getMessage()));
		}

		return false;
	}

	private static boolean addNewAdmin(String email, String password){ return addNewAdmin(email, password, "0");}
	private static boolean addNewAdmin(String email, String password, String initialBalance)
	{
		try (Database db = Database.getInstance()) {
			String date = LocalDateTime.now().toString();
			String[] data = new String[]{
					Hashing.sha256().hashString(email+password+date, StandardCharsets.UTF_8).toString(),
					email,
					password,
					initialBalance,
					date
			};

			return db.insert(TB_NAME, FIELDS, data) != null;
		}
	}
}
