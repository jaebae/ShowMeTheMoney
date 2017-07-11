package com.sungjae.app.showmethemoney.service.api.model;

public class Balance {
    float mBitMoney;
    float mRealMoney;
    Currency mCurrency;

    private final static float mMinRate = 2.f; // 2%

    public Balance(float bitMoney, float realMoeny, Currency currency) {
        mBitMoney = bitMoney;
        mRealMoney = realMoeny;
        mCurrency = currency;
    }

   public float getBitMoney() {
       return mBitMoney;
   }

    public float getRealMoney() {
        return mRealMoney;
    }


    @Override
    public String toString() {
        return "mBitMoney = " + mBitMoney + "\nmRealMoeny = " + mRealMoney ;
    }

    public float getSellDiff() {
        float unit = 0.0f;

        float bitReal = mBitMoney * mCurrency.mBuy;
        float diff = bitReal - mRealMoney;

        if (diff > 0) {
            float percent = (diff / mRealMoney) * 100.f;
            System.out.println("krw = " + mRealMoney + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > 2.f) {
                unit = (diff / 2) / mCurrency.mBuy;
                int Unit = (int) (unit * mCurrency.getMinTradeRate());
                unit = (Unit / mCurrency.getMinTradeRate());
            }
        }

        return unit;
    }

    public float getBuyDiff() {
        float unit = 0.0f;

        float bitReal = mBitMoney * mCurrency.mSell;
        float diff = mRealMoney - bitReal;

        if (diff > 0) {
            float percent = (diff / mRealMoney) * 100.f;
            System.out.println("krw = " + mRealMoney + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > 2.f) {
                unit = (diff / 2) / mCurrency.mSell;
                int Unit = (int) (unit * mCurrency.getMinTradeRate());
                unit = (Unit / mCurrency.getMinTradeRate());
            }
        }

        return unit;
    }

}
