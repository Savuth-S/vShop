package sav.vshop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.logging.Logger;

public class Config
{        
	private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    //LOGICA
	private static boolean loaded = false;

    public static String dbIp = "", dbPort = "", dbUsr = "", dbPwrd = "";
    public static List<String> names = new ArrayList<>();

    public static boolean load()
    {
		try{
			if (!loaded){
    			Properties props = new Properties();
				props.load(Config.class.getResourceAsStream("/res/config.properties"));

				dbIp = props.getProperty("db_ip");
				dbPort = props.getProperty("db_port");
				dbUsr = props.getProperty("db_user");
				dbPwrd = props.getProperty("db_password");
    			
				names.add(props.getProperty("db_name")); //0
				names.add(props.getProperty("tb_admins_name")); //1
				names.add(props.getProperty("tb_users_name"));  //2
				names.add(props.getProperty("tb_catalog_name"));  //3
				names.add(props.getProperty("tb_log_name"));  //4

				loaded = true;
			}
		}catch (FileNotFoundException e){
			LOGGER.severe(String.format("COULDN'T FIND CONFIG FILE: %s", e.getMessage()));
		}catch (IOException e){
			LOGGER.severe(String.format("FAILED TO ACCESS FILE: %s", e.getMessage()));
		}catch (Exception e){
			LOGGER.severe("UNKNOWN EXCEPTION");
			e.printStackTrace();
		}

		return loaded;
	}
}
