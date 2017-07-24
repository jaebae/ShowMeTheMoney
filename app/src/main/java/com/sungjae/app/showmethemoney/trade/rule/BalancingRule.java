package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingsActiviy;
import com.sungjae.app.showmethemoney.data.DataMap;

class BalancingRule extends ITradeRule {

    @Override
    public boolean isEnabled() {
        return ConfigurationConstants.getEnabledBalancedRule();
    }

    protected float getSellAmount() {
        float out = Math.abs(Math.min(0, calcAmount(mBuyValue, ConfigurationConstants.getSellRate())));
        System.out.println("getSellAmount = " + out + "\n====\n");

        return out;
    }

    protected float getBuyAmount() {
        float out = Math.max(0, calcAmount(mSellValue, ConfigurationConstants.getBuyRate()));
        System.out.println("getBuyAmount = " + out + "\n====\n");
        return out;
    }

    @Override
    public void footer() {

    }

    @Override
    public void header() {

    }

    private float calcAmount(float coinUnitValue, float diffRate) {
        float amount = 0.0f;
        float bitReal = mCoinAmount * coinUnitValue;
        float total = bitReal + mMoneyValueAvail;


        float investRate = DataMap.readFloat(SettingsActiviy.SETTING_HEADER + ConfigurationConstants.INVEST_RATE);
        float expectedCoinValue = total * investRate;
        float diff = expectedCoinValue - bitReal;
        System.out.println("investRate = " + investRate);
        System.out.println("mMoneyValueAvail = " + mMoneyValueAvail);
        System.out.println("bitReal = " + bitReal);
        System.out.println("total = " + total);
        System.out.println("expectedCoinValue = " + expectedCoinValue);
        //30 -> coin, money will be 70
        //money+bit == 100;
        //expected con = total * 0.3


        if (diff != 0) {
            float percent = (diff / Math.min(mMoneyValueAvail, bitReal)) * 100.f;
            System.out.println("\ndiff = " + diff + "\nPercent = " + percent);

            if (Math.abs(percent) >= diffRate) {
                amount = (diff) / coinUnitValue;
                int Amount = (int) (amount * mTradeUnit);
                amount = (Amount / mTradeUnit);
            }
        }

        return amount;

    }

}
