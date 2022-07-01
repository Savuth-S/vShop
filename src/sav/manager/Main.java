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
		if (makeDatabase()){
			LOGGER.info("DATABASE MADE!");
		}else{
			LOGGER.severe("FAILED TO MAKE DATABASE");
		}
	}

	private static boolean makeDatabase()
	{
		boolean bLoadConfig = Config.load(),
				bDbMade = false,
				bTbMade = false;

		if (bLoadConfig){
			try (DbHelper db = DbHelper.getInstance()){
				if (db.isConnected("MAIN MAKEDATABASE")) {
					LOGGER.warning(String.format("DATABASE ALREADY EXISTS: %s", Config.sDbName));
					dbMade = true;
				}else{
					LOGGER.info("Making database...");
					dbMade = db.connect("") && db.execute(String.format("CREATE DATABASE %s", Config.dbName)) != null;
					db.close();
					dbMade = dbMade && db.connect();
				}

				tbMade = dbMade && makeTables(db);
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		return loadConfig && dbMade && tbMade;
	}

	private static boolean makeTables(DbHelper db)
	{
		if(db.isConnected("MAIN MAKETABLES")){
			AtomicBoolean tablesMade = new AtomicBoolean(true);

			List<Table> tables = new ArrayList<>();
			int USERS = 0, ADMINS = 1, CATALOG = 2, LOG = 3;
			tables.add(new Users(db)); //0
			tables.add(new Admins(db)); //1
			tables.add(new Catalog(db)); //2
			tables.add(new Log(db)); //3
			
			tables.forEach((tb) -> tablesMade.set(tb.create() && tablesMade.get()));

			return adminExists((Admins) tables.get(ADMINS)) && tablesMade.get(); 
		}

		return false;
	}

	private static boolean adminExists(Admins admins)
	{
		if (!admins.entryExists(admins.ID, "1")){
			LOGGER.info("Cannot find default admin will try to make it.");
			admins.addNewAdmin("admin@admin.adm", "admin");
			
			//TODO 
			//Future check if properly insert then return false if not
			return admins.entryExists(admins.ID, "1");
		}

		return true;
	}
}
