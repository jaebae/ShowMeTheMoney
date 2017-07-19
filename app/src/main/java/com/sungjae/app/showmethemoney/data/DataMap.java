package com.sungjae.app.showmethemoney.data;

import java.util.HashMap;

/**
 * Created by bennj on 2017-07-19.
 */

public class DataMap {

    static HashMap<String,Boolean> mapBoolean = new HashMap<>();
    static HashMap<String,Float> mapFloat = new HashMap<>();
    static HashMap<String,String> mapString = new HashMap<>();
    public static void writeBoolean(String key, Boolean value)
    {
        mapBoolean.put(key,value);
    }
    public static Boolean readBoolean(String key)
    {
        return mapBoolean.get(key);
    }

    public static void writeFloat(String key, Float value)
    {
        mapFloat.put(key,value);
    }
    public static Float readFloat(String key)
    {
        return mapFloat.get(key);
    }


    public static void writeString(String key, String value)
    {
        mapString.put(key,value);
    }
    public static String readString(String key)
    {
        return mapString.get(key);
    }
}
