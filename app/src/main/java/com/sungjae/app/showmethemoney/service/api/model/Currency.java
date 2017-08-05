package com.sungjae.app.showmethemoney.service.api.model;

import java.util.HashMap;

public class Currency {
    private float mBuy;
    private float mSell;
    private String mCoinType;

    HashMap<String, Float> mMinTrade = new HashMap<>();

    public Currency(String coinType, float buy, float sell) {
        mCoinType = coinType;
        mBuy = buy;
        mSell = sell;

        mMinTrade.put("BCH", 1000.0f);
        mMinTrade.put("BTC", 1000.0f);
        mMinTrade.put("ETH", 100.0f);
        mMinTrade.put("DASH", 100.0f);
        mMinTrade.put("LTC", 10.0f);
        mMinTrade.put("ETC", 10.0f);
        mMinTrade.put("XRP", 10.0f);
    }

    @Override
    public String toString() {
        return "Buy = " + mBuy + "\nSell = " + mSell;
    }

    public String getCoinType() {
        return mCoinType;
    }

    public float getMinTradeUnit() {
        return mMinTrade.get(mCoinType);
    }

    public float getBuy() {
        return mBuy;
    }

    public float getSell() {
        return mSell;
    }

}