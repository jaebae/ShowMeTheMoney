package com.sungjae.app.showmethemoney.activity.setting;

import android.content.Context;
import android.preference.PreferenceManager;

import com.sungjae.app.showmethemoney.data.DataMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationConstants {
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final Properties prop;
    public static final String INVEST_RATE = "investRate";
    public static final String ENABLE_MONEY_KEEPER = "enableMoneyKeeper";
    public static final String KEEP_VALUE = "KeepValue";
    public static final String ENABLE_BALANCED = "enableBalanced";
    public static final String SELL_VALUE = "sellValue";
    public static final String BUY_VALUE = "buyValue";
    public static final String TOTAL_INPUT = "totalInput";
    public static final String ENABLE_CUTOFF = "enableCutoff";
    public static final String HIGH_CUT = "HighCut";
    public static final String LOW_CUT = "LowCut";

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
    private static final float INVEST_RATE_DEFAULT = 0.5f;

    private static Context mCtx;
    public static void init(Context ctx)
    {
        mCtx=ctx;

    }
    static public void syncSettingsToDataMap()
    {
        DataMap.writeFloat(SettingsActiviy.SETTING_HEADER +ConfigurationConstants.INVEST_RATE, ConfigurationConstants.getInvestRate());
        DataMap.writeFloat(SettingsActiviy.SETTING_HEADER +ConfigurationConstants.KEEP_VALUE, ConfigurationConstants.getKeepValueMoneyKeeper());


        DataMap.writeFloat(SettingsActiviy.SETTING_HEADER +ConfigurationConstants.HIGH_CUT, ConfigurationConstants.getCutoffHigh());
        DataMap.writeFloat(SettingsActiviy.SETTING_HEADER +ConfigurationConstants.LOW_CUT, ConfigurationConstants.getCutoffLow());

    }
    public static float getSellRate()
    {
        return getPreferenceFloat(SELL_VALUE,MIN_DIFF_VALUE);
    }
    public static float getBuyRate()
    {
        return getPreferenceFloat(BUY_VALUE,MIN_DIFF_VALUE);
    }
    public static float getInvestRate()
    {
        return getPreferenceFloat(INVEST_RATE,INVEST_RATE_DEFAULT);
    }


    public static Float getTotalInput(){ return getPreferenceFloat(TOTAL_INPUT,0f); }

    public static boolean getEnabledMoneyKeeper(){ return getPreferenceBoolean(ENABLE_MONEY_KEEPER,false); }
    public static Float getKeepValueMoneyKeeper(){ return getPreferenceFloat(KEEP_VALUE,0f); }


    public static boolean setEnabledBalancedRule(boolean onOff){ return setPreferenceBoolean(ENABLE_BALANCED,onOff); }
    public static boolean getEnabledBalancedRule(){ return getPreferenceBoolean(ENABLE_BALANCED,false); }


    public static boolean getEnabledCutoffRule(){ return getPreferenceBoolean(ENABLE_CUTOFF,false); }
    public static Float getCutoffHigh(){ return getPreferenceFloat(HIGH_CUT,9999999999f); }
    public static Float getCutoffLow(){ return getPreferenceFloat(LOW_CUT,-99999999999f); }


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
    private static boolean setPreferenceBoolean(String key, Boolean value)
    {

        if(mCtx!=null) value = PreferenceManager.getDefaultSharedPreferences(mCtx).edit().putBoolean(key,value).commit();
        System.out.println(key+" : "+value);
        syncSettingsToDataMap();
        return value;
    }
}
