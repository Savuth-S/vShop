package main.java;

import com.sun.tools.javac.util.Log;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import main.java.database.Database;
import main.java.database.Table;
import main.java.database.tables.*;

public class Main
{

	//MAIN PROGRAM
        public static void main(String[] args)
        {
	        Database db = Database.getInstance( new Database(
				"jdbc:mariadb://172.25.16.1:3306/",
				"root",
				"root"));
		
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
                tables.put("", new Admins());
                tables.put("", new Catalog());
                tables.put("", new Log());

	        try (Database db = Database.getInstance()){
			if (db != null){
				return db.init(tables);
			}
		}

		return false;
	}
}
