package main.java;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

public class Main
{

	//MAIN PROGRAM
	public static void main(String[] args)
	{
    	Config config = new Config();
		try { config.load();}
		catch(Exception e) { e.printStackTrace();}

	    Database db = Database.makeInstance( 
			new Database(
				String.format("jdbc:mariadb://%s:%s/",
        						config.dbIp, config.dbPort),
				config.dbUsr,
				config.dbPwrd
			)
		);
		
		try {
		LOGGER.info("Cannot find database will try to make it");

				conn = DriverManager.getConnection(url, usr, pwd);
				isConnected = make() && connect();
				close();
			} catch (SQLException e) {
				LOGGER.severe("FAILED TO CREATE DATABASE", e);
			} catch (Exception e) {
				e.printStackTrace();
			}

    	
		Map<String, Table> tables = new HashMap<>();
                tables.put("", new Users());
                tables.put("TODOADM", new Admins());
                // tables.put("", new Catalog());
                tables.put("", new Log());

	        try (Database db = Database.getInstance()){
			if (db != null){
				return db.init(tables);
			}
		}

		return false;
	}
}
