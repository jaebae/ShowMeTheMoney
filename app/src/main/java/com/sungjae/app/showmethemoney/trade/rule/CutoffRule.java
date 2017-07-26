package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;


public class CutoffRule extends ITradeRule {

    boolean bActivated;

    @Override
    public boolean isEnabled() {
        return ConfigurationConstants.getEnabledCutoffRule();
    }

    @Override
    float getSellAmount() {
        float cutOff = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT);
        if (mBuyValue > cutOff) {
            bActivated = true;
            return mCoinAmount;
        }
        return 0;
    }

    @Override
    float getBuyAmount() {
        float cutOff = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT);
        if (cutOff > mSellValue) {
            float moneyValueRaw = DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW);
            bActivated = true;
            float buyAmount = checkAmount(moneyValueRaw / mBuyValue);
            return buyAmount;
        }
        return 0;
    }

    @Override
    public void footer() {
        if (bActivated) {
            ConfigurationConstants.setEnabledBalancedRule(false);
            DataMap.writeString(DataMapKey.NOTIFICATION_CONTENT, "CUT OFF ACTIVATED");
            System.out.println("CUT OFF ACTIVATED -> turn off balanced rule");
        }
    }

    @Override
    public void header() {
        bActivated = false;
    }
}
