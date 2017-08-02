package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BalanceTest {

    @Test
    public void sellDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance b = new Balance(106.f, 100.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.5f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.0f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(3.f));

        c = new Currency("ETH", 0.1f, 1.f);
        b = new Balance(1060.f, 100.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.5f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.0f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(30.f));

        c = new Currency("ETH", 1f, 1.f);
        b = new Balance(1000.f, 1000.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.4f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.0f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(200.f));
    }

    private void testBR(Currency c, Balance b) {

        DataMap.writeFloat(DataMap.BUY_VALUE, c.getBuy());
        DataMap.writeFloat(DataMap.SELL_VALUE, c.getSell());
        DataMap.writeFloat(DataMap.MIN_TRADE_UNIT, c.getMinTradeUnit());
        DataMap.writeFloat(DataMap.COIN_AMOUNT, b.getBitAmount());
        DataMap.writeFloat(DataMap.MONEY_VALUE_RAW, b.getRealMoney());
        DataMap.writeFloat(DataMap.MONEY_VALUE_AVAIL, b.getRealMoney());


        Float totalValue = b.getBitAmount() + b.getRealMoney();
        DataMap.writeFloat(DataMap.TOTAL_VALUE_RAW, totalValue);

        BalancingRule tr = new BalancingRule();
        tr.execute();
    }


    @Test
    public void buyDiffTest() throws Exception {
        Currency c = new Currency("ETH", 1.f, 1.f);
        Balance b = new Balance(100.f, 106.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.5f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(3.f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(0.f));

        c = new Currency("ETH", 0.1f, 1.f);
        b = new Balance(1000.f, 1060.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.5f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(30.f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(0.f));

        c = new Currency("ETH", 1f, 1f);
        b = new Balance(1000.f, 1000.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE, 0.7f);
        testBR(c, b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(400.f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(0.f));


    }
}