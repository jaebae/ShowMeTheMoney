package com.sungjae.app.showmethemoney.trade.rule;

import com.sungjae.app.showmethemoney.data.DataMap;


abstract public class ITradeRule {
    protected float mMoneyValueAvail;
    protected float mCoinAmount;
    protected float mBuyValue;
    protected float mSellValue;
    protected float mTradeUnit;

    private void getValue() {
        mMoneyValueAvail = DataMap.readFloat(DataMap.MONEY_VALUE_AVAIL);
        mCoinAmount = DataMap.readFloat(DataMap.COIN_AMOUNT);

        mBuyValue = DataMap.readFloat(DataMap.BUY_VALUE);
        mSellValue = DataMap.readFloat(DataMap.SELL_VALUE);
        mTradeUnit = DataMap.readFloat(DataMap.MIN_TRADE_UNIT);
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

        DataMap.writeFloat(DataMap.TRADE_BUY_AMOUNT, buyAmount);
        DataMap.writeFloat(DataMap.TRADE_SELL_AMOUNT, sellAmount);

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
