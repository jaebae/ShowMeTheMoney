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
    private static final float MIN_DIFF_VALUE = 3.0f;

    private static Context mCtx;
    public static void init(Context ctx)
    {
        mCtx=ctx;
    }
    public static float getSellRate()
    {
        return getPreferenceFloat("sellValue",MIN_DIFF_VALUE);
    }
    public static float getBuyRate()
    {
        return getPreferenceFloat("buyValue",MIN_DIFF_VALUE);
    }

    public static boolean getEnabledMoneyKeeper(){ return getPreferenceBoolean("enableMoneyKeeper",false); }
    public static Float getKeepValueMoneyKeeper(){ return getPreferenceFloat("KeepValue",0f); }


    public static boolean getEnabledBalancedRule(){ return getPreferenceBoolean("enableBalanced",false); }

    private static float getPreferenceFloat(String key, Float defaultValue)
    {
        String value = defaultValue.toString();
        if(mCtx!=null) value = PreferenceManager.getDefaultSharedPreferences(mCtx).getString(key,defaultValue.toString());
        System.out.println(key+" : "+value);
        return Float.parseFloat(value);
    }
    private static boolean getPreferenceBoolean(String key, Boolean defaultValue)
    {
        Boolean value = defaultValue;

        if(mCtx!=null) value = PreferenceManager.getDefaultSharedPreferences(mCtx).getBoolean(key,defaultValue);
        System.out.println(key+" : "+value);
        return value;
    }

}
