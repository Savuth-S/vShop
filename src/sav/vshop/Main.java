package main.java;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import javax.xml.catalog.Catalog;

import main.java.database.Database;
import main.java.database.Table;
import main.java.database.tables.*;
import main.java.utils.Config;

public class Main
{

	//MAIN PROGRAM
        public static void main(String[] args)
        {
                Config config = new Config();
                try{    config.load();
                }catch(Exception e){ e.printStackTrace();}

	        Database db = Database.getInstance( new Database(
				String.format("jdbc:mariadb://%s:%s/",
                                                            config.dbIp,
                                                            config.dbPort),
				config.dbUsr,
				config.dbPwrd));
		
		if (init()) {
		        Logger.getLogger(Main.class.getName())
					.info("INIT SUCCES!");
    	        }else {
			Logger.getLogger(Main.class.getName())
					.severe("INIT FAILED!");
    	        }
    	
        }
	
	//LOGIC
	private static boolean init()
        {       
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
