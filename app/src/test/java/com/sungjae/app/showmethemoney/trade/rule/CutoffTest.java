package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CutoffTest {

    @Test
    public void sellDiffTest() throws Exception {
        Currency c = new Currency("ETH", 249f, 249f);
        Balance b = new Balance(106.f, 100.f, c);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, 300f);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, 200f);
        testCutoff(c,b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.0f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(0f));

        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, 300f);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, 200f);
        c = new Currency("ETH", 200f, 199f);
        testCutoff(c,b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.5f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(0f));

        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, 300f);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, 200f);
        c = new Currency("ETH", 301f, 299f);
        testCutoff(c,b);
        assertThat(DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT), is(0.f));
        assertThat(DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT), is(106f));
    }

    private void testCutoff(Currency c, Balance b)
    {

        DataMap.writeFloat(DataMap.BUY_VALUE, c.getBuy());
        DataMap.writeFloat(DataMap.SELL_VALUE, c.getSell());
        DataMap.writeFloat(DataMap.MIN_TRADE_UNIT, c.getMinTradeUnit());
        DataMap.writeFloat(DataMap.COIN_AMOUNT, b.getBitAmount());
        DataMap.writeFloat(DataMap.MONEY_VALUE_RAW, b.getRealMoney());
        DataMap.writeFloat(DataMap.MONEY_VALUE_AVAIL, b.getRealMoney());
        DataMap.writeFloat(DataMap.TRADE_BUY_AMOUNT,0f);
        DataMap.writeFloat(DataMap.TRADE_SELL_AMOUNT,0f);


        Float totalValue = b.getBitAmount() +  b.getRealMoney();
        DataMap.writeFloat(DataMap.TOTAL_VALUE_RAW, totalValue);

        CutoffRule tr = new CutoffRule();
        tr.header();
        tr.execute();
        tr.footer();
    }


}