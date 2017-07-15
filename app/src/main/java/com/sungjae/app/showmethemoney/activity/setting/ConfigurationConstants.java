package com.sungjae.app.showmethemoney.activity.setting;

import android.content.Context;
import android.preference.PreferenceManager;

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
    private static final String MIN_DIFF_VALUE = "3.0";

    private static Context mCtx;
    public static void init(Context ctx)
    {
        mCtx=ctx;
    }
    public static float getSellDiffMinRate()
    {
        return getDiff("sellValue");
    }
    public static float getBuyDiffMinRate()
    {
        return getDiff("buyValue");
    }
    private static float getDiff(String key)
    {
        String value = MIN_DIFF_VALUE;
        if(mCtx!=null) value = PreferenceManager.getDefaultSharedPreferences(mCtx).getString(key,MIN_DIFF_VALUE);
        System.out.println(key+" : "+value);
        return Float.parseFloat(value);
    }
}
