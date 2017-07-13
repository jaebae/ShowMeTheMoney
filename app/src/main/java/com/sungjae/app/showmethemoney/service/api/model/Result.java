package com.sungjae.app.showmethemoney.service.api.model;

public class Result {
    public long mDate;
    public String mTrade;
    public float mUnit;
    public float mPrice;
    public float mAmount;

    public Result(long date, String trade, float unit, float price, float amount) {
        mDate = date;
        mTrade = trade;
        mUnit = unit;
        mPrice = price;
        mAmount = amount;
    }
}
