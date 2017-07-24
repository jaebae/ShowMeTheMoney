package com.sungjae.app.showmethemoney.trade.rule;

public class TradeRuleFactory {


    static public ITradeRule[] getRules() {
        return new ITradeRule[]{
                new CutoffRule(),//have to faster than balancing rule
                new BalancingRule()
        };
    }
}
