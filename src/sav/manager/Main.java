package sav.manager;

import java.sql.SQLException;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;

import java.util.*;
import sav.manager.Table;
import sav.manager.tables.Admins;
import sav.manager.tables.Catalog;
import sav.manager.tables.Log;
import sav.manager.tables.Users;
import sav.vshop.Config;
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
				tbMade = false;

		if (loadConfig){
			try (DbHelper db = DbHelper.getInstance()){
				if (db.getConnection().isValid(30)) {
					LOGGER.warning(String.format("DATABASE ALREADY EXISTS: %s %nCONNECTION: %s", Config.dbName, db.connect()));
					dbMade = true;
				}else{
					LOGGER.info("Making database...");
					db.setConnection("");
					dbMade = db.execute(String.format("CREATE DATABASE %s", Config.dbName)) != null;
					
					db.setConnection(Config.dbName);
				}
				
				tbMade = makeTables(db);
			}
		}

		return loadConfig && dbMade && tbMade;
	}

	private static boolean makeTables(DbHelper db)
	{
		AtomicBoolean tablesMade = new AtomicBoolean(true);
		boolean adminExists = false;

		if(db.connect()){				
			List<Table> tables = new ArrayList<>();
                	
			tables.add(new Users()); //0
            tables.add(new Admins()); //1
            tables.add(new Catalog()); //2
        	tables.add(new Log()); //3
				
		    tables.forEach((tb) -> tablesMade.set(tablesMade.get() && tb.create()));
				
			adminExists = tablesMade.get() && tables.get(1).entryExists(tables.get(1).ID, "1");
		}

		return false;
	}
}
