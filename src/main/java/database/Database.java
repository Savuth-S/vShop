package main.java.database;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import main.java.database.tables.*;

public class Database implements AutoCloseable
{
	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

	//SINGLETON
	private static Database database = null;

	public static Database getInstance(){database.connect(); return getInstance(database); }
	public static Database getInstance(Database db)
	{
		if (database == null && db != null){
			database = db;
		}

		return database;
	}

	//DATABASE INIT
	private final String url;
	private final String usr;
	private final String pwd;
	
	public static final String DB_NAME = "test";
	
	
	public Database(String url, String user, String password)
	{   
		this.url = url;
                this.usr = user;
                this.pwd = password;
	}

	Connection conn = null;
	public boolean init() 
	{
		boolean isConnected = false;
		AtomicBoolean areTablesMade = new AtomicBoolean(false);
		boolean doesAdminExists = false;

		if (!connect()) {
			try {

				LOGGER.info("Cannot find database will try to make it");

				conn = DriverManager.getConnection(url, usr, pwd);
				isConnected = make() && connect();
				close();
			} catch (SQLException e) {
				LOGGER.severe(String.format("FAILED TO CREATE DATABASE: %s", e.getMessage()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			isConnected = true;
		}

		if (isConnected) {
			Table[] tables = new Table[]{
					new Users(),
					new Catalog(),
					new Log(),
					new Admins()
			};
			areTablesMade.set(true);
			Arrays.stream(tables).forEach(tb -> areTablesMade.set(areTablesMade.get() && tb.create()));

			doesAdminExists = Admins.makeDefaultAdmin();
		}

		return isConnected && areTablesMade.get() && doesAdminExists;
	}
	
	//INNER LOGIC
	public boolean connect()
    {
		boolean isConnected = false;

		try{
			if (conn == null){
				conn = DriverManager.getConnection(url + DB_NAME, usr, pwd);
			}

			if (conn.isClosed()){
				conn = DriverManager.getConnection(url + DB_NAME, usr, pwd);
			}

			if (!conn.isValid(30)){
				conn = DriverManager.getConnection(url + DB_NAME, usr, pwd);
			}

			isConnected = conn.isValid(30) ;
		}catch (SQLException e){ LOGGER.severe(String.format("CANNOT CONNECT TO DATABASE: %s", e.getMessage()));
		}catch (Exception e){ e.printStackTrace(); }

//		LOGGER.log(Level.INFO, "Connection?: {0} ", isConnected);

		return isConnected;
	}
	
	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				if (conn.isValid(30)) {
					conn.close();
				} else {
					LOGGER.severe("THERE IS NO VALID CONNECTION");
				}
			} else if (conn == null) {
				LOGGER.severe("THERE IS NO EXISTING CONNECTION");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	private boolean make() {
		try{ return execute(String.format("CREATE DATABASE %s", DB_NAME)) != null;
		}catch (SQLException e){ e.printStackTrace();}

		return false;
	}

	public ResultSet execute(String sql) throws SQLException {
		if (conn != null && conn.isValid(30)) {
				try (Statement cmnd = conn.createStatement()){
					return cmnd.executeQuery(sql);
				}catch (SQLException e){ LOGGER.warning(String.format("FAILED TO EXECUTE COMMAND: %s %nMESSAGE: %s", sql, e.getMessage())); }
			}else{ LOGGER.severe("THERE IS NO EXISTING CONNECTION"); }

		return null;
	}

	public ResultSet insert(String tableName, String[] fields, String[] data){
		try{ if (conn != null && conn.isValid(30)) {
				String query = String.format("INSERT INTO %s (%s) VALUES (%s)",
						tableName,
						formatFields(fields),
						formatFieldsQueryCount(fields));

				try (PreparedStatement insertValues = conn.prepareStatement(query)){
					for (int i = 0; i < formatFields(fields).split(",").length; i++){
						insertValues.setString(i+1, data[i]);
					}

					return insertValues.executeQuery();
				}catch (SQLException e){ LOGGER.warning(String.format("FAILED TO INSERT ENTRY: %s %nMESSAGE: %s", query, e.getMessage())); }
			}else{ LOGGER.severe("THERE IS NO EXISTING CONNECTION"); }
		}catch (SQLException e){ e.printStackTrace(); }

		return null;
	}

	private String formatFields(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(Table.ID)){
				if (field.equals(fields[1])){
					sb.append(field);
				}else{
					sb.append(String.format(", %s",field));
				}
			}
		});

		return sb.toString();
	}

	private String formatFieldsQueryCount(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(Table.ID)){
				if (field.equals(fields[1])){
					sb.append('?');
				}else{
					sb.append(", ?");
				}
			}
		});

		return sb.toString();
	}
}
