package com.sungjae.app.showmethemoney.service.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

/**
 * Created by bennj on 2017-07-18.
 */

class BalancingRule extends TradeRule {
    public BalancingRule(Currency currency, Balance balance) {
        super(currency, balance);
    }

    public float getSellAmount() {
        return calcAmount(mCurrency.getBuy(), ConfigurationConstants.getSellRate());
    }

    public float getBuyAmount() {
        return calcAmount(mCurrency.getSell(), ConfigurationConstants.getBuyRate());
    }

    private float calcAmount(float coinValue, float diffRate)
    {
        float amount = 0.0f;
        float bitReal = mBalance.getBitMoney() * coinValue;
        float diff = Math.abs(mBalance.getRealMoney() - bitReal);

        if (diff > 0) {
            float percent = (diff / mBalance.getRealMoney()) * 100.f;
            System.out.println("krw = " + mBalance.getRealMoney() + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > diffRate) {
                amount = (diff / 2) / coinValue;
                int Amount = (int) (amount * mCurrency.getMinTradeUnit());
                amount = (Amount / mCurrency.getMinTradeUnit());
            }
        }

        return amount;

    }
}
