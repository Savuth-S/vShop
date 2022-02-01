package main.java.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config
{
        //SINGLETON
        public static Config config = null;

        public static Config getInstance()
        {
                if (config == null){
                        config = new Config();
                        try{
							config.load();
               
						}catch(Exception e){e.printStackTrace();}
				}
                return config;
        }

        
        //LOGICA
        public String dbIp = "", dbPort = "", dbUsr = "", dbPwrd = "";
        
        public String tbAdmins = "", tbCatalog = "", tbLog = "", tbUsers = "";


        Properties props = new Properties();
        public void load() throws FileNotFoundException, IOException
        {
                props.load(new FileInputStream("res/config.properties"));
                
                dbIp = props.getProperty("db_ip");
                dbPort = props.getProperty("db_port");
                
                dbUsr = props.getProperty("db_user");
                dbPwrd = props.getProperty("db_password");
        }
}
