package sav.manager;

import com.sun.tools.javac.util.Log;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.logging.Logger;

import javax.xml.catalog.Catalog;

import sav.manager.Table;
import sav.manager.tables.*;
import sav.utils.Config;
import sav.vshop.database.DbHelper;

public class Main
{
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	//MAIN PROGRAM
	public static void main(String[] args)
	{
		try{
			if (makeDatabase()){
				LOGGER.info("DATABASE MADE!");
			}else{
				LOGGER.severe("FAILED TO MAKE DATABASE");
		}catch (SQLException e){ e.printStackTrace();}
	}

	private static boolean makeDatabase() throws SQLException
	{	
		AtomicBoolean isMade = true;

		if (Config.load()){
			try (DbHelper db = DbHelper.getInstance()){
				if(db != null && !db.connect()){
					LOGGER.info("Will try to make the database");
					
					db.setConnection(Config.url);
					isMade = isMade && db.execute(String.format("CREATE DATABASE %s", DB_NAME)) != null;
				}

				if(isMade && db.connect()){				
					AtomicBoolean areTablesMade = new AtomicBoolean(true);
					Map<String, Table> tables = new HashMap<>();
                	
					tables.put(Config.tbNameUsrs, new Users());
                	tables.put("TODOADM", new Admins());
                	tables.put("", new Catalog());
                	tables.put("", new Log());
				
		        	tables.forEach((nm, tb) -> areTablesMade.set(areTablesMade.get() && tb.create()));
				
					isMade = isMade && areTablesMade.get() && tables.get("TODOADM").entryExists(ID, "1");
	
				}
			}
		}
	}

}
