package com.sungjae.app.showmethemoney.service.rule;

import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

/**
 * Created by bennj on 2017-07-18.
 */

public class TradeRuleFactory {


    static public TradeRule getRule(Currency currency, Balance balance)
    {
        return new BalancingRule(currency, balance);
    }
}
