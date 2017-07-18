package com.sungjae.app.showmethemoney.service.api.model;

import com.sungjae.app.showmethemoney.service.rule.TradeRule;
import com.sungjae.app.showmethemoney.service.rule.TradeRuleFactory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BalanceTest {

    @Test
    public void sellDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance balance = new Balance(106.f, 100.f, c);
        TradeRule tr = TradeRuleFactory.getRule(c,balance);
        assertThat(tr.getSellDiff(), is(3.f));

        c = new Currency("ETH", 0.1f, 1.f);
        balance = new Balance(1060.f, 100.f, c);
        tr = TradeRuleFactory.getRule(c,balance);
        assertThat(tr.getSellDiff(), is(30.f));
    }


    @Test
    public void buyDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance balance = new Balance(100.f, 106.f, c);
        TradeRule tr = TradeRuleFactory.getRule(c,balance);
        assertThat(tr.getBuyDiff(), is(3.f));

        c = new Currency("ETH", 0.1f, 1.f);
        balance = new Balance(1000.f, 1060.f, c);
        tr = TradeRuleFactory.getRule(c,balance);
        assertThat(tr.getBuyDiff(), is(30.f));
    }
}