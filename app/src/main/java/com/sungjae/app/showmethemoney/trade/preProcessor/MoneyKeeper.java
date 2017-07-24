package com.sungjae.app.showmethemoney.trade.preProcessor;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;


public class MoneyKeeper implements IDataUpdater {
    private float mRawMoney;
    private float mCoinValue;
    private float mTotalValue;
    private boolean mEnabled;
    private float mRequestToKeep;

    @Override
    public void getValue() {
        mRawMoney = DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW);
        mCoinValue = DataMap.readFloat(DataMapKey.COIN_AMOUNT);
        mTotalValue = DataMap.readFloat(DataMapKey.TOTAL_VALUE_RAW);

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
            mRequestToKeep = mRawMoney;
            DataMap.writeString(DataMapKey.ERROR_TOAST_CONTENT, "not enough money = " + (mRequestToKeep - mRawMoney));
        }

        Float availMoney = mRawMoney - mRequestToKeep;
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_AVAIL, availMoney);
        Float availTotal = availMoney + mCoinValue;
        DataMap.writeFloat(DataMapKey.TOTAL_VALUE_AVAIL, availTotal);
    }
}
