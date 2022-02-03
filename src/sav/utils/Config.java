package sav.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config
{        
    //LOGICA
	private static boolean hasLoaded = false;
	
    public static String dbIp = "", dbPort = "", dbUsr = "", dbPwrd = "";
        
    public static String tbAdmins = "", tbCatalog = "", tbLog = "", tbUsers = "";

    public static boolean load() throws FileNotFoundException, IOException
    {
		if (!hasLoaded){
    		Properties props = new Properties();
			props.load(new FileInputStream("res/config.properties"));
                
			dbIp = props.getProperty("db_ip");
			dbPort = props.getProperty("db_port");
                
			dbUsr = props.getProperty("db_user");
    		dbPwrd = props.getProperty("db_password");
    		
			hasLoaded = true;
		}
		
		return hasLoaded;
	}
}
