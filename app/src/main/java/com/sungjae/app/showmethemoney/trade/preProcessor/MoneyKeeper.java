package com.sungjae.app.showmethemoney.trade.preProcessor;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.IDataUpdater;


public class MoneyKeeper implements IDataUpdater {
    private float mRawMoney;
    private float mCoinValue;
    private float mTotalValue;
    private boolean mEnabled;
    private float mRequestToKeep;

    @Override
    public void getValue() {
        mRawMoney = DataMap.readFloat(DataMap.MONEY_VALUE_RAW);
        mCoinValue = DataMap.readFloat(DataMap.COIN_AMOUNT);
        mTotalValue = DataMap.readFloat(DataMap.TOTAL_VALUE_RAW);

        mEnabled = ConfigurationConstants.getEnabledMoneyKeeper();

        if (mEnabled) {
            mRequestToKeep = ConfigurationConstants.getKeepValueMoneyKeeper();
        } else {
            mRequestToKeep = 0;
        }
    }

    @Override
    public void update() {

        if (mRequestToKeep > mRawMoney) {
            DataMap.writeString(DataMap.ERROR_TOAST_CONTENT, "not enough money = " + (mRequestToKeep - mRawMoney));
        }

        Float availMoney = mRawMoney - mRequestToKeep;
        DataMap.writeFloat(DataMap.MONEY_VALUE_AVAIL, availMoney);
        Float availTotal = availMoney + mCoinValue;
        DataMap.writeFloat(DataMap.TOTAL_VALUE_AVAIL, availTotal);
    }
}
