package com.sungjae.app.showmethemoney.activity.setting;

import android.content.Context;
import android.preference.PreferenceManager;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.log.MyLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationConstants {

    private final static String CONFIG_FILE_NAME = "config.properties";
    private final static Properties PROPERTIES;
    private final static String CURRENCY = "currency";
    public final static String INVEST_RATE = "investRate";
    public final static String ENABLE_MONEY_KEEPER = "enableMoneyKeeper";
    public final static String KEEP_VALUE = "KeepValue";
    public final static String ENABLE_BALANCED = "enableBalanced";
    public final static String SELL_VALUE = "sellValue";
    public final static String BUY_VALUE = "buyValue";
    public final static String TOTAL_INPUT = "totalInput";
    public final static String ENABLE_CUTOFF = "enableCutoff";
    public final static String HIGH_CUT = "HighCut";
    public final static String LOW_CUT = "LowCut";

    static {
        PROPERTIES = new Properties();
        try (InputStream stream = ConfigurationConstants.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            PROPERTIES.load(stream);
        } catch (IOException e) {
            MyLog.d(ConfigurationConstants.class, "init PROPERTIES Error : " + e);
        }
    }

    public static final String CONNECT_KEY = PROPERTIES.getProperty("CON_KEY");
    public static final String SECRET_KEY = PROPERTIES.getProperty("SECRET_KEY");
    private static final float MIN_DIFF_VALUE = 3.0f;
    private static final float INVEST_RATE_DEFAULT = 0.5f;
    private static final String CURRENCY_DEFAULT = "ETH";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;

    }

    static public void syncSettingsToDataMap() {
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, ConfigurationConstants.getInvestRate());
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.KEEP_VALUE, ConfigurationConstants.getKeepValueMoneyKeeper());

        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, ConfigurationConstants.getCutoffHigh());
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, ConfigurationConstants.getCutoffLow());

    }

    public static float getSellRate() {
        return getPreferenceFloat(SELL_VALUE, MIN_DIFF_VALUE);
    }

    public static float getBuyRate() {
        return getPreferenceFloat(BUY_VALUE, MIN_DIFF_VALUE);
    }

    public static float getInvestRate() {
        return getPreferenceFloat(INVEST_RATE, INVEST_RATE_DEFAULT);
    }

    public static String getCurrency() {
        return getPreferenceString(CURRENCY, CURRENCY_DEFAULT);
    }


    public static Float getTotalInput() {
        return getPreferenceFloat(TOTAL_INPUT, 0f);
    }

    public static boolean getEnabledMoneyKeeper() {
        return getPreferenceBoolean(ENABLE_MONEY_KEEPER, false);
    }

    public static Float getKeepValueMoneyKeeper() {
        return getPreferenceFloat(KEEP_VALUE, 0f);
    }


    public static boolean setEnabledBalancedRule(boolean onOff) {
        return setPreferenceBoolean(ENABLE_BALANCED, onOff);
    }
    public static boolean setEnabledCufOff(boolean onOff) {
        return setPreferenceBoolean(ENABLE_CUTOFF, onOff);
    }
    public static boolean getEnabledBalancedRule() {
        return getPreferenceBoolean(ENABLE_BALANCED, false);
    }


    public static boolean getEnabledCutoffRule() {
        return getPreferenceBoolean(ENABLE_CUTOFF, false);
    }

    public static Float getCutoffHigh() {
        return getPreferenceFloat(HIGH_CUT, 9999999999f);
    }

    public static Float getCutoffLow() {
        return getPreferenceFloat(LOW_CUT, -99999999999f);
    }

    public static boolean setCutoffHigh(Float value) {
        return setPreferenceFloat(HIGH_CUT, value);
    }

    public static boolean setCutoffLow(Float value) {
        return setPreferenceFloat(LOW_CUT, value);
    }

    private static float getPreferenceFloat(String key, Float defaultValue) {
        String value = defaultValue.toString();
        if (mContext != null) {
            value = PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, defaultValue.toString());
        }
        MyLog.d(ConfigurationConstants.class, key + " : " + value);
        return Float.parseFloat(value);
    }

    private static boolean getPreferenceBoolean(String key, Boolean defaultValue) {
        Boolean value = defaultValue;

        if (mContext != null) {
            value = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, defaultValue);
        }
        MyLog.d(ConfigurationConstants.class, key + " : " + value);
        return value;
    }

    private static boolean setPreferenceBoolean(String key, Boolean value) {
        if (mContext != null) {
            value = PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(key, value).commit();
        }
        MyLog.d(ConfigurationConstants.class, key + " : " + value);
        syncSettingsToDataMap();
        return value;
    }

    private static String getPreferenceString(String key, String value) {
        if (mContext != null) {
            value = PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, value);
        }
        MyLog.d(ConfigurationConstants.class, key + " : " + value);
        return value;
    }

    private static boolean setPreferenceFloat(String key, Float value) {
        if (mContext != null) {
             PreferenceManager.getDefaultSharedPreferences(mContext).edit().putFloat(key, value).commit();
        }
        MyLog.d(ConfigurationConstants.class, key + " : " + value);
        syncSettingsToDataMap();
        return true;
    }

}
