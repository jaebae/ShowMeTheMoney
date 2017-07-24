package com.sungjae.app.showmethemoney.data;

import java.util.HashMap;

public class DataMap {
    static HashMap<String, Float> sMapFloat = new HashMap<>();
    static HashMap<String, String> sMapString = new HashMap<>();

    public static void writeFloat(String key, Float value) {
        sMapFloat.put(key, value);
    }

    public static Float readFloat(String key) {
        return sMapFloat.get(key);
    }


    public static void writeString(String key, String value) {
        sMapString.put(key, value);
    }

    public static String readString(String key) {
        return sMapString.get(key);
    }
}
