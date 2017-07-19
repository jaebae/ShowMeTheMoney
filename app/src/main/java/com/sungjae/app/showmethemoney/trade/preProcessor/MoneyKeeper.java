package com.sungjae.app.showmethemoney.trade.preProcessor;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;

/**
 * Created by bennj on 2017-07-19.
 */

public class MoneyKeeper implements IDataUpdater {
    float rawMoney;
    float coinValue;
    float totalValue;
    boolean enabled;
    float requestToKeep;

    @Override
    public void getValue() {
        rawMoney = DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW);
        coinValue = DataMap.readFloat(DataMapKey.COIN_AMOUNT);
        totalValue = DataMap.readFloat(DataMapKey.TOTAL_VALUE_RAW);

        enabled = ConfigurationConstants.getEnabledMoneyKeeper();
        requestToKeep = ConfigurationConstants.getKeepValueMoneyKeeper();
    }

    @Override
    public void update() {
        if(enabled==false)
            return;

        if(requestToKeep > rawMoney )
        {
            requestToKeep = rawMoney;
            DataMap.writeString(DataMapKey.ERROR_TOAST_CONTENT,"not enough money = "+(requestToKeep-rawMoney));
        }


        Float availMoney = rawMoney - requestToKeep;
        DataMap.writeFloat(DataMapKey.MONEY_VALUE_AVAIL, availMoney);
        Float availTotal = availMoney + coinValue;
        DataMap.writeFloat(DataMapKey.TOTAL_VALUE_AVAIL, availMoney);

    }
}
