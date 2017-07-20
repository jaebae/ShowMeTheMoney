package com.sungjae.app.showmethemoney.trade.rule;

/**
 * Created by bennj on 2017-07-18.
 */

public class TradeRuleFactory {


    static public ITradeRule[] getRules()
    {
        return new ITradeRule[]{
                new CutoffRule(),//have to faster than balancing rule
                new BalancingRule()
        };
    }
}
