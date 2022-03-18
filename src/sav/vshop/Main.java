package sav.vshop;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import sav.vshop.database.DbHelper;
import sav.vshop.Config;

public class Main
{

	//MAIN PROGRAM
        public static void main(String[] args)
        {
                Config config = new Config();
                try{    config.load();
                }catch(Exception e){ e.printStackTrace();}

	        // DbHelper db = DbHelper.getInstance();
		
		// if (init()) {
		//         Logger.getLogger(Main.class.getName())
		// 			.info("INIT SUCCES!");
    	        // }else {
		// 	Logger.getLogger(Main.class.getName())
		// 			.severe("INIT FAILED!");
    	        // }
    	
        }
	

        //{       
                //Map<String, Table> tables = new HashMap<>();
                //tables.put("", new Users());
                //tables.put("TODOADM", new Admins());
                //// tables.put("", new Catalog());
                //tables.put("", new Log());

	//        try (DbHelper db = DbHelper.getInstance()){
	//		if (db != null){
	//			//return db.init(tables);
	//		}
	//	}

	//	return false;
	//}
}
