package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;


abstract public class ITradeRule {
    protected float mMoneyValueAvail;
    protected float mCoinAmount;
    protected float mBuyValue;
    protected float mSellValue;
    protected float mTradeUnit;

    private void getValue() {
        mMoneyValueAvail = DataMap.readFloat(DataMapKey.MONEY_VALUE_AVAIL);
        mCoinAmount = DataMap.readFloat(DataMapKey.COIN_AMOUNT);

        mBuyValue = DataMap.readFloat(DataMapKey.BUY_VALUE);
        mSellValue = DataMap.readFloat(DataMapKey.SELL_VALUE);
        mTradeUnit = DataMap.readFloat(DataMapKey.MIN_TRADE_UNIT);
    }

    public void execute() {
        getValue();

        float buyAmount = getBuyAmount();
        float sellAmount = 0f;

        if (buyAmount == 0f) {
            sellAmount = getSellAmount();
        }

        if (sellAmount > mCoinAmount) {
            sellAmount = checkAmount(mCoinAmount);
        }

        DataMap.writeFloat(DataMapKey.TRADE_BUY_AMOUNT, buyAmount);
        DataMap.writeFloat(DataMapKey.TRADE_SELL_AMOUNT, sellAmount);

    }

    float checkAmount(float amount) {
        int Amount = (int) (amount * mTradeUnit);
        amount = (Amount / mTradeUnit);
        return amount;
    }

    abstract public boolean isEnabled();

    abstract float getSellAmount();

    abstract float getBuyAmount();

    abstract public void footer();

    abstract public void header();
}
