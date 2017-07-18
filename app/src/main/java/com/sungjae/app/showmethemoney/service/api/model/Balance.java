package com.sungjae.app.showmethemoney.service.api.model;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;

public class Balance {
    private float mBitMoney;
    private float mRealMoney;
    private Currency mCurrency;

    //private final static float mMinRate = 3.f; // 3%

    public Balance(float bitMoney, float realMoney, Currency currency) {
        mBitMoney = bitMoney;
        mRealMoney = realMoney;
        mCurrency = currency;
    }

    public float getBitMoney() {
        return mBitMoney;
    }

    public float getRealMoney() {
        return mRealMoney;
    }

    public Currency getCurrency() {
        return mCurrency;
    }


    @Override
    public String toString() {
        return "mBitMoney = " + getBitMoney() + "\nmRealMoeny = " + getRealMoney();
    }



}
