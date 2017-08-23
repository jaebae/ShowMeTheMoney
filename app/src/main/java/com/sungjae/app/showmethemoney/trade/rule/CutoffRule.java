package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.data.DataMap;


public class CutoffRule extends ITradeRule {

    boolean mActivated;

    @Override
    public boolean isEnabled() {
        return ConfigurationConstants.getEnabledCutoffRule();
    }

    @Override
    float getSellAmount() {
        float cutOff = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT);
        if (mBuyValue > cutOff) {
            mActivated = true;
            ConfigurationConstants.setCutoffHigh(9999999999f);
            return mCoinAmount;
        }
        return 0;
    }

    @Override
    float getBuyAmount() {
        float cutOff = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT);
        if (cutOff > mSellValue) {
            float moneyValueRaw = DataMap.readFloat(DataMap.MONEY_VALUE_RAW);
            mActivated = true;
            ConfigurationConstants.setCutoffLow(0f);
            float buyAmount = checkAmount(moneyValueRaw / mBuyValue);
            return buyAmount;
        }
        return 0;
    }

    @Override
    public void footer() {
        if (mActivated) {
            ConfigurationConstants.setEnabledBalancedRule(false);
            DataMap.writeString(DataMap.NOTIFICATION_CONTENT, "CUT OFF ACTIVATED");
            System.out.println("CUT OFF ACTIVATED -> turn off balanced rule");

        }
    }

    @Override
    public void header() {
        mActivated = false;
    }
}
