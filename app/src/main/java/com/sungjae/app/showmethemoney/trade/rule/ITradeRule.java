package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

/**
 * Created by bennj on 2017-07-18.
 */

abstract public class ITradeRule {
    float moneyValueAvail;
    float coinAmount;
    float buyValue;
    float sellValue;
    float tradeUnit;


    private void getValue() {
        moneyValueAvail = DataMap.readFloat(DataMapKey.MONEY_VALUE_AVAIL);
        coinAmount = DataMap.readFloat(DataMapKey.COIN_AMOUNT);

        buyValue = DataMap.readFloat(DataMapKey.BUY_VALUE);
        sellValue = DataMap.readFloat(DataMapKey.SELL_VALUE);
        tradeUnit = DataMap.readFloat(DataMapKey.MIN_TRADE_UNIT);
    }

    public void execute() {

        getValue();

        float buyAmount = getBuyAmount();
        float sellAmount = getSellAmount();

        DataMap.writeFloat(DataMapKey.TRADE_BUY_AMOUNT, buyAmount);
        DataMap.writeFloat(DataMapKey.TRADE_SELL_AMOUNT, sellAmount);

    }
    abstract public boolean isEnabled();
    abstract float getSellAmount();
    abstract float getBuyAmount();
}
