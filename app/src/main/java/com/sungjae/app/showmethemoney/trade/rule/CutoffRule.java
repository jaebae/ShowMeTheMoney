package com.sungjae.app.showmethemoney.trade.rule;

import android.app.NotificationManager;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingsActiviy;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;

/**
 * Created by bennj on 2017-07-20.
 */

public class CutoffRule extends ITradeRule {

    boolean bActivated;

    @Override
    public boolean isEnabled() {
        return ConfigurationConstants.getEnabledCutoffRule();
    }

    @Override
    float getSellAmount() {
        float cutOff = DataMap.readFloat(SettingsActiviy.SETTING_HEADER + ConfigurationConstants.HIGH_CUT);
        if (buyValue > cutOff) {
            bActivated = true;
            return coinAmount;
        }
        return 0;
    }

    @Override
    float getBuyAmount() {
        float cutOff = DataMap.readFloat(SettingsActiviy.SETTING_HEADER + ConfigurationConstants.LOW_CUT);
        if (cutOff > sellValue) {
            float moneyValueRaw = DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW);
            bActivated = true;
            float buyAmount = checkAmount(moneyValueRaw / buyValue);
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
