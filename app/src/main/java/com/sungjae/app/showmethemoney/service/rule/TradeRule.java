package com.sungjae.app.showmethemoney.service.rule;

import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

/**
 * Created by bennj on 2017-07-18.
 */

public abstract class TradeRule {
    Currency mCurrency;
    Balance mBalance;
    public TradeRule(Currency currency, Balance balance) {
        mCurrency=currency;
        mBalance=balance;
    }
    abstract public float getSellAmount();
    abstract public float getBuyAmount();
}
