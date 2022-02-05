package sav.vshop.database;

import java.util.logging.Logger;

import java.sql.*;
import java.util.*;
import sav.utils.Config;

public class DbHelper implements AutoCloseable
{
	private static final Logger LOGGER = Logger.getLogger(DbHelper.class.getName());

	//SINGLETON
	private static DbHelper database = null;
	
	public static DbHelper getInstance()
	{
		if (database == null){
			database =  new DbHelper();


		}

		database.connect();
		return database;
	}

	//INNER LOGIC
	private static final String URL = String.format("jdbc:mariadb://%s:%s/", Config.dbIp, Config.dbPort);

	private static final String DB_NAME = Config.dbName;
	private static final String USR = Config.dbUsr;
	private static final String PWD = Config.dbPwrd;
	
	private Connection conn = null;
	public void setConnection(String ip, String port, String database)
	{
		try {
			conn = DriverManager.getConnection(
					String.format("jdbc:mariadb://%s:%s/%s", ip, port, database),
					USR,
					PWD
				);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean connect()
    {
		boolean isConnected = false;

		try{
			if (conn == null){
				conn = DriverManager.getConnection(URL + DB_NAME, USR, PWD);
			}

			if (conn.isClosed()){
				conn = DriverManager.getConnection(URL + DB_NAME, USR, PWD);
			}

			if (!conn.isValid(30)){
				conn = DriverManager.getConnection(URL + DB_NAME, USR, PWD);
			}

			isConnected = conn.isValid(30) ;
		
		}catch (SQLException e){ 
			LOGGER.severe(String.format("CANNOT CONNECT TO DATABASE: %s", e.getMessage()));
		}catch (Exception e){ 
			e.printStackTrace(); 
		}

		return isConnected;
	}
	
	public void close() 
	{
		try{ if (conn != null && !conn.isClosed()){
				if (conn.isValid(30)){ conn.close(); }  
				else { LOGGER.severe("THERE IS NO VALID CONNECTION"); }
			 }else if (conn == null){
			 	LOGGER.severe("THERE IS NO EXISTING CONNECTION");}
		
		}catch (SQLException e){ e.printStackTrace(); }
	}
	
	public ResultSet execute(String sql) throws SQLException 
	{
		if (conn != null && conn.isValid(30)) {
			try (Statement cmnd = conn.createStatement()) {
				return cmnd.executeQuery(sql);
			}
		}
		
		LOGGER.severe(String.format("THERE IS NO VALID CONNECTION: %s", conn));
		return null;
	}

	public ResultSet insert(String table, String[] fields, Object[] data)
	{
		String query = String.format("INSERT INTO %s %s",
				table,
				prepareFields(fields));

		try (PreparedStatement insertValues = conn.prepareStatement(query)){
			for (int i = 0; i < fields.length; i++){
				if (data[i] instanceof byte[] bytes) {
					insertValues.setBytes(i + 1, bytes);
				} else if (data[i] instanceof Integer integer) {
					insertValues.setInt(i + 1, integer);
				} else if (data[i] instanceof String string) {
					insertValues.setString(i + 1, string);
				} else {
					LOGGER.warning(String.format("INSERT VALUE TYPE NOT FOUND: %s", data[i]));
				}
			}

			return insertValues.executeQuery();
		}catch (SQLException e){ LOGGER.warning(String.format("FAILED TO INSERT ENTRY: %s %nMESSAGE: %s", query, e.getMessage())); }

		return null;
	}


	private String prepareFields(String[] fieldsList)
	{
		StringBuilder fields = new StringBuilder(), fieldsCount = new StringBuilder();
		
		Arrays.stream(fieldsList).forEach(field -> {
			if (field.equals(fieldsList[1])){
				fields.append(field);
				fieldsCount.append('?');
			}else{
				fields.append(String.format(", %s", field));
				fieldsCount.append(", ?");
			}
		});

		return String.format("(%s) VALUES (%s)", fields.toString(), fieldsCount.toString());
	}

	private String formatFieldsQueryCount(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(field -> {
			if (field.equals(fields[1])){
					sb.append('?');
			}else{
					sb.append(", ?");
			}
		});

		return sb.toString();
	}
}
