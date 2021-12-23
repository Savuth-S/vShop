package main.java;

import java.util.logging.Logger;

import main.java.database.Database;

public class Main
{
	//MAIN PROGRAM
	public static void main(String[] args)
    {
		Database.getInstance( new Database(
				"jdbc:mariadb://localhost:3306/",
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
	private static boolean init() {
		try (Database db = Database.getInstance()){
			if (db != null){
				return db.init();
			}
		}
		
		return false;
	}
}
