package main.java.manager;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;

import main.java.manager.Table;
import main.java.utils.Config;
import main.java.database.DbHelper;

public class Main
{
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	//MAIN PROGRAM
	public static void main(String[] args)
	{
		try{ if (makeDatabase()){
			LOGGER.info("DATABASE MADE!");}
		}catch (SQLException e){ 
			LOGGER.severe(String.format("FAILED TO MAKE DATABASE: %s", e.getMessage()));
		}
	}

	private static boolean makeDatabase() throws SQLException
	{	
		boolean loadConfig = Config.load();
		boolean dbMade = false;
		AtomicBoolean tablesMade = new AtomicBoolean(true);
		boolean adminExists = false;

		if (loadConfig){
			try (DbHelper db = DbHelper.getInstance()){
				if(!db.connect()){
					LOGGER.info("Will try to make the database");
					
					db.setConnection(Config.dbIp, Config.dbPort, "");
					dbMade = db.execute(String.format("CREATE DATABASE %s", DB_NAME)) != null;
				}else{
					LOGGER.warning("DATABASE ALREADY EXISTS");
					return false;
				}

				if(dbMade && db.connect()){				
					Map<String, Table> tables = new HashMap<>();
                	
					tables.put(Config.tbNameUsrs, new Users());
                	tables.put("TODOADM", new Admins());
                	tables.put("", new Catalog());
                	tables.put("", new Log());
				
		        	tables.forEach((nm, tb) -> tablesMade.set(tablesMade.get() && tb.create()));
				
					adminExists = tablesMade.get() ? tables.get("TODOADM").entryExists(ID, "1") : false;
	
				}
			}
		}

		return loadConfig && dbMade && tablesMade && adminExists;
	}

}
