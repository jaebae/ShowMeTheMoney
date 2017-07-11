package com.sungjae.app.showmethemoney.service.api.model;

public class Result {
    public long mDate;
    public String mTrade;
    public String mUnit;
    public String mPrice;
    public String mAmount;

    public Result(long date, String trade, String unit, String price, String amount) {
        mDate = date;
        mTrade = trade;
        mUnit = unit;
        mPrice = price;
        mAmount = amount;
    }
}
