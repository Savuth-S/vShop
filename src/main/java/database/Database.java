package main.java.database;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import main.java.database.tables.AdminLog;
import main.java.database.tables.Admins;
import main.java.database.tables.Catalog;
import main.java.database.tables.Log;
import main.java.database.tables.Table;
import main.java.database.tables.Users;

public class Database
{
	//DATABASE INIT
	private final String url, usr, pwd;
	
	private static final String DB_NAME = "test";
	
	
	public Database(String url, String user, String password)
	{   
		this.url = url;
        this.usr = user;
        this.pwd = password;
	}
	
	
	Connection conn = null;
	public boolean init() 
	{
		boolean isConnect = false;
		
		if (connect()) {
			isConnect = true;
		} else {
			try{
				Logger.getLogger(Database.class.getName())
						.warning("Cannot find database will try to make it");
				
				conn = DriverManager.getConnection(url, usr, pwd);	
				isConnect = make() ? connect() : isConnect;
			}catch (SQLException e){
				Logger.getLogger(Database.class.getName())
						.severe(String.format("failed TO CREATE DATABASE: %s", e.getMessage()));
			}catch (Exception e) { e.printStackTrace();} 
		}
			
		if (isConnect) {
			Table[] tables = new Table[]{
					new Users(),
					new Catalog(),
					new Log(),
					new AdminLog(),
					new Admins()
			};

			Arrays.stream(tables).forEach(tb -> tb.create(conn));

		}
		
		return isConnect;
	}
	
	//INNER LOGIC
	public boolean connect()
    {
        try{ 
        	conn = DriverManager.getConnection(url+DB_NAME, usr, pwd);

//			Logger.getLogger(Database.class.getName()).
//					info(String.format("Succes! {}", conn));
            
            return true;
        }catch (SQLException e){
        	Logger.getLogger(Database.class.getName())
				.severe(String.format("CANNOT TO CONNECT TO DATABASE: %s", e.getMessage()));
        }catch (Exception e) { e.printStackTrace();} 
        
        return false;
    }
	
	public boolean close() {
		try {
			if (conn != null){
				conn.close();
				return true;
			}else{
				Logger.getLogger(Database.class.getName())
					.severe("THERE IS NO EXISTING CONNECTION");
			}
		} catch (SQLException e) { e.printStackTrace(); }

		return false;
	}
	
	private boolean make() {
		if (conn != null) {
			try (Statement cmnd = conn.createStatement()){
				conn.setAutoCommit(true);

				String sql = String.format("CREATE DATABASE %s", DB_NAME);
				cmnd.execute(sql);

				return true;
			}catch (SQLException e) {
				Logger.getLogger(Database.class.getName())
						.severe(String.format("FAILED TO MAKE TO DATABASE: %s", e.getMessage()));
			}
		}else{
			Logger.getLogger(Database.class.getName())
					.severe("THERE IS NO EXISTING CONNECTION");
		}

		return false;
	}
}
