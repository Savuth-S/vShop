package sav.manager;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;

import jdk.javadoc.internal.doclets.formats.html.Table;

import main.java.utils.Config;
import sav.manager.tables.Admins;
import sav.manager.tables.Catalog;
import sav.manager.tables.Log;
import sav.manager.tables.Users;
import sav.vshop.database.DbHelper;

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
		boolean loadConfig = Config.load(),
				dbMade = false,
				adminExists = false;
		AtomicBoolean tablesMade = new AtomicBoolean(true);

		if (loadConfig){
			try (DbHelper db = DbHelper.getInstance()){
				if(!db.connect()){
					LOGGER.info("Will try to make the database");
					
					db.setConnection(Config.dbIp, Config.dbPort, "");
					dbMade = db.execute(String.format("CREATE DATABASE %s", Config.dbName)) != null;
				}else{
					LOGGER.warning("DATABASE ALREADY EXISTS");
					dbMade = true;
				}

				if(dbMade && db.connect()){				
					Map<String, Table> tables = new HashMap<>();
                	
					tables.put("", new Users());
                	tables.put("TODOADM", new Admins());
                	tables.put("", new Catalog());
                	tables.put("", new Log());
				
		        	tables.forEach((nm, tb) -> tablesMade.set(tablesMade.get() && tb.create()));
				
					adminExists = tablesMade.get() && tables.get("TODOADM").entryExists(Table.ID, "1");
	
				}
			}
		}

		return loadConfig && dbMade && tablesMade.get() && adminExists;
	}

}
