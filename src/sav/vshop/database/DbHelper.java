package sav.vshop.database;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;

import java.sql.*;
import java.util.*;

import sav.utils;

public class Database implements AutoCloseable
{
	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

	//SINGLETON
	private static Database database = null;
	
	public static Database getInstance() throws RuntimeException
	{
		if (database == null){
			database =  new Database();

			// RuntimeException e =  new RuntimeException("Database not initialized"); 
			// LOGGER.severe(String.format("DATABASE HAS NOT BEEN INITIALIZED: %s", e.getMessage()));
			// throw e;
		}

		database.connect();
		return database;
	}

	//DATABASE INIT
	private final String url = String.format("jdbc:mariadb://%s:%s/", Config.dbIp, Config.dbPort);
	private final String usr = Config.dbUsr;
	private final String pwd = Config.dbPwrd;
	private Connection conn = null;

	public static final String DB_NAME = "wposshop";
	
	public void setConnection(Connection conn){ this.conn = conn; }

	
	
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
	
	public void close() 
	{
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

	

	public ResultSet execute(String sql) throws SQLException {
		if (conn != null && conn.isValid(30)) {
				try (Statement cmnd = conn.createStatement()){
					return cmnd.executeQuery(sql);
				}catch (SQLException e){ LOGGER.warning(String.format("FAILED TO EXECUTE COMMAND: %s %nMESSAGE: %s", sql, e.getMessage())); }
			}else{ LOGGER.severe("THERE IS NO EXISTING CONNECTION"); }

		return null;
	}
}
