package com.sungjae.app.showmethemoney;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationConstants {
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final Properties prop;
    static {
        InputStream stream = ConfigurationConstants.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        prop = new Properties();
        try {
            prop.load(stream);
        } catch (IOException e){
            System.out.println("Error : "+e);
        }
    }

    public static final String CONNECT_KEY = prop.getProperty("CON_KEY");
    public static final String SECRET_KEY = prop.getProperty("SECRET_KEY");

}
