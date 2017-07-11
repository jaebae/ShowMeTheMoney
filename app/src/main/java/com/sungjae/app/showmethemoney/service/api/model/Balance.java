package com.sungjae.app.showmethemoney.service.api.model;

public class Balance {
    private float mBitMoney;
    private float mRealMoney;
    private Currency mCurrency;

    private final static float mMinRate = 2.f; // 2%

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

    public float getSellDiff() {
        float unit = 0.0f;

        float bitReal = getBitMoney() * getCurrency().getBuy();
        float diff = bitReal - getRealMoney();

        if (diff > 0) {
            float percent = (diff / getRealMoney()) * 100.f;
            System.out.println("krw = " + getRealMoney() + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > mMinRate) {
                unit = (diff / 2) / getCurrency().getBuy();
                int Unit = (int) (unit * getCurrency().getMinTradeRate());
                unit = (Unit / getCurrency().getMinTradeRate());
            }
        }

        return unit;
    }

    public float getBuyDiff() {
        float unit = 0.0f;

        float bitReal = getBitMoney() * getCurrency().getSell();
        float diff = getRealMoney() - bitReal;

        if (diff > 0) {
            float percent = (diff / getRealMoney()) * 100.f;
            System.out.println("krw = " + getRealMoney() + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > mMinRate) {
                unit = (diff / 2) / getCurrency().getSell();
                int Unit = (int) (unit * getCurrency().getMinTradeRate());
                unit = (Unit / getCurrency().getMinTradeRate());
            }
        }

        return unit;
    }

}
