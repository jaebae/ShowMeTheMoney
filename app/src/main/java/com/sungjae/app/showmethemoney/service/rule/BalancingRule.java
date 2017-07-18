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

    public float getSellDiff() {
        float unit = 0.0f;

        float bitReal = mBalance.getBitMoney() * mCurrency.getBuy();
        float diff = bitReal - mBalance.getRealMoney();

        if (diff > 0) {
            float percent = (diff / mBalance.getRealMoney()) * 100.f;
            System.out.println("krw = " + mBalance.getRealMoney() + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > ConfigurationConstants.getSellDiffMinRate()) {
                unit = (diff / 2) / mCurrency.getBuy();
                int Unit = (int) (unit * mCurrency.getMinTradeRate());
                unit = (Unit / mCurrency.getMinTradeRate());
            }
        }

        return unit;
    }

    public float getBuyDiff() {
        float unit = 0.0f;

        float bitReal = mBalance.getBitMoney() * mCurrency.getSell();
        float diff = mBalance.getRealMoney() - bitReal;

        if (diff > 0) {
            float percent = (diff / mBalance.getRealMoney()) * 100.f;
            System.out.println("krw = " + mBalance.getRealMoney() + "\nbit = " + bitReal + "\ndiff = " + diff + "\nPercent = " + percent);

            if (percent > ConfigurationConstants.getBuyDiffMinRate()) {
                unit = (diff / 2) / mCurrency.getSell();
                int Unit = (int) (unit * mCurrency.getMinTradeRate());
                unit = (Unit / mCurrency.getMinTradeRate());
            }
        }

        return unit;
    }
}
