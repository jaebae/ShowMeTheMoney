package com.sungjae.app.showmethemoney.data;

import java.util.HashMap;

public class DataMap {
    public final static String BUY_VALUE = "buyValue";
    public final static String SELL_VALUE = "sellValue";
    public final static String AVG_COIN_VALUE = "avgCoinValue";
    public final static String MIN_TRADE_UNIT = "minTradeUnit";
    public final static String COIN_AMOUNT = "coinAmount";
    public final static String MONEY_VALUE_RAW = "moneyValueRaw";
    public final static String TOTAL_VALUE_RAW = "totalValueRaw";
    //----------money keeper
    public final static String MONEY_VALUE_AVAIL = "availableMoney";
    public final static String TOTAL_VALUE_AVAIL = "totalAvailValue";
    //----------- trade rule
    public final static String TRADE_SELL_AMOUNT = "trSellAmount";
    public final static String TRADE_BUY_AMOUNT = "trBuyAmount";
    public final static String ERROR_TOAST_CONTENT = "errToastContent";
    public final static String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
    public final static String NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT";
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
