package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.activity.setting.SettingsActivity;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
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
        assertThat(DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT), is(0.0f));
        assertThat(DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT), is(0f));

        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, 300f);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, 200f);
        c = new Currency("ETH", 200f, 199f);
        testCutoff(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT), is(0.5f));
        assertThat(DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT), is(0f));

        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.HIGH_CUT, 300f);
        DataMap.writeFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.LOW_CUT, 200f);
        c = new Currency("ETH", 301f, 299f);
        testCutoff(c,b);
        assertThat(DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT), is(0.f));
        assertThat(DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT), is(106f));
    }

    private void testCutoff(Currency c, Balance b)
    {

        DataMap.writeFloat(DataMapKey.BUY_VALUE, c.getBuy());
        DataMap.writeFloat(DataMapKey.SELL_VALUE, c.getSell());
        DataMap.writeFloat(DataMapKey.MIN_TRADE_UNIT, c.getMinTradeUnit());
        DataMap.writeFloat(DataMapKey.COIN_AMOUNT, b.getBitAmount());
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_RAW, b.getRealMoney());
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_AVAIL, b.getRealMoney());
        DataMap.writeFloat(DataMapKey.TRADE_BUY_AMOUNT,0f);
        DataMap.writeFloat(DataMapKey.TRADE_SELL_AMOUNT,0f);


        Float totalValue = b.getBitAmount() +  b.getRealMoney();
        DataMap.writeFloat(DataMapKey.TOTAL_VALUE_RAW, totalValue);

        CutoffRule tr = new CutoffRule();
        tr.header();
        tr.execute();
        tr.footer();
    }


}