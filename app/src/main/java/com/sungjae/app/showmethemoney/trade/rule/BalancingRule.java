package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;

/**
 * Created by bennj on 2017-07-18.
 */

class BalancingRule extends ITradeRule {


    @Override
    boolean isEnabled() {
        return ConfigurationConstants.getEnabledBalancedRule();
    }

    protected float getSellAmount() {
        return calcAmount(buyValue, ConfigurationConstants.getSellRate());
    }

    protected float getBuyAmount() {
        return calcAmount(sellValue, ConfigurationConstants.getBuyRate());
    }

    private float calcAmount(float coinValue, float diffRate)
    {
        float amount = 0.0f;
        float bitReal = coinAmount * coinValue;
        float diff = Math.abs(moneyValueAvail - bitReal);

        if (diff > 0) {
            float percent = (diff / moneyValueAvail) * 100.f;
            System.out.println("krw = " + moneyValueAvail + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > diffRate) {
                amount = (diff / 2) / coinValue;
                int Amount = (int) (amount * tradeUnit);
                amount = (Amount / tradeUnit);
            }
        }

        return amount;

    }

}
