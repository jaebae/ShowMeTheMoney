package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;
import com.sungjae.app.showmethemoney.trade.rule.BalancingRule;
import com.sungjae.app.showmethemoney.trade.rule.ITradeRule;
import com.sungjae.app.showmethemoney.trade.rule.TradeRuleFactory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BalanceTest {

    @Test
    public void sellDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance b = new Balance(106.f, 100.f, c);
        testBR(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT), is(3.f));

        c = new Currency("ETH", 0.1f, 1.f);
        b = new Balance(1060.f, 100.f, c);
        testBR(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT), is(30.f));
    }

    private void testBR(Currency c, Balance b)
    {

        DataMap.writeFloat(DataMapKey.BUY_VALUE, c.getBuy());
        DataMap.writeFloat(DataMapKey.SELL_VALUE, c.getSell());
        DataMap.writeFloat(DataMapKey.MIN_TRADE_UNIT, c.getMinTradeUnit());
        DataMap.writeFloat(DataMapKey.COIN_AMOUNT, b.getBitAmount());
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_RAW, b.getRealMoney());
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_AVAIL, b.getRealMoney());


        Float totalValue = b.getBitAmount() +  b.getRealMoney();
        DataMap.writeFloat(DataMapKey.TOTAL_VALUE_RAW, totalValue);

        BalancingRule tr = new BalancingRule();
        tr.execute();
    }


    @Test
    public void buyDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance b = new Balance(100.f, 106.f, c);
        testBR(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT), is(3.f));

        c = new Currency("ETH", 0.1f, 1.f);
        b = new Balance(1000.f, 1060.f, c);
        testBR(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT), is(30.f));
    }
}