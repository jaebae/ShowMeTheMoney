package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.log.MyLog;

class BalancingRule extends ITradeRule {

    @Override
    public boolean isEnabled() {
        return ConfigurationConstants.getEnabledBalancedRule();
    }

    protected float getSellAmount() {
        float out = Math.abs(Math.min(0, calcAmount(mBuyValue, ConfigurationConstants.getSellRate())));
        MyLog.d(this, "getSellAmount = " + out);

        return out;
    }

    protected float getBuyAmount() {
        float out = Math.max(0, calcAmount(mSellValue, ConfigurationConstants.getBuyRate()));
        MyLog.d(this, "getBuyAmount = " + out );
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


        float investRate = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE);
        float expectedCoinValue = total * investRate;
        float diff = expectedCoinValue - bitReal;
        MyLog.d(this, "investRate = " + investRate);
        MyLog.d(this, "mMoneyValueAvail = " + mMoneyValueAvail);
        MyLog.d(this, "bitReal = " + bitReal);
        MyLog.d(this, "total = " + total);
        MyLog.d(this, "expectedCoinValue = " + expectedCoinValue);
        //30 -> coin, money will be 70
        //money+bit == 100;
        //expected con = total * 0.30


        if (diff != 0) {
            float percent = (diff / Math.min(mMoneyValueAvail, bitReal)) * 100.f;
            MyLog.d(this, "diff = " + diff + " Percent = " + percent);

            if (Math.abs(percent) >= diffRate) {
                amount = (diff) / coinUnitValue;
                int Amount = (int) (amount * mTradeUnit);
                amount = (Amount / mTradeUnit);
            }
        }

        return amount;

    }

}
