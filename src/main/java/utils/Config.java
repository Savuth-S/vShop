package main.java.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import java.util.logging.Logger;

public class Config
{        
	private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    //LOGICA
	private static boolean hasLoaded = false;

	public static String dbName = "";
    public static String dbIp = "", dbPort = "", dbUsr = "", dbPwrd = "";
    public static String tbAdmins = "", tbCatalog = "", tbLog = "", tbUsers = "";

    public static boolean load()
    {
		try{
			if (!hasLoaded){
    			Properties props = new Properties();
				props.load(new FileInputStream("res/config.properties"));

				dbIp = props.getProperty("db_ip");
				dbPort = props.getProperty("db_port");
				
				dbUsr = props.getProperty("db_user");
				dbPwrd = props.getProperty("db_password");
    		
				hasLoaded = true;
			}
		}catch (FileNotFoundException e){
			LOGGER.severe(String.format("COULDN'T FIND CONDIG FILE: %s", e.getMessage()));
		}catch (IOException e){
			LOGGER.severe(String.format("FAILED TO ACCES FILE: %s", e.getMessage()));
		}

		return hasLoaded;
	}
}
