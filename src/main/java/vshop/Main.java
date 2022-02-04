package main.java;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import main.java.database.DbHelper;
import main.java.manager.Table;
import main.java.manager.tables.*;
import main.java.utils.Config;

public class Main
{

	//MAIN PROGRAM
        public static void main(String[] args)
        {
                Config config = new Config();
                try{    config.load();
                }catch(Exception e){ e.printStackTrace();}

	        DbHelper db = DbHelper.getInstance();
		
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

	        try (DbHelper db = DbHelper.getInstance()){
			if (db != null){
				return db.init(tables);
			}
		}

		return false;
	}
}
