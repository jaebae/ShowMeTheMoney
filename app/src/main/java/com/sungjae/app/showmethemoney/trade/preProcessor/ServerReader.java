package com.sungjae.app.showmethemoney.trade.preProcessor;

import android.util.Log;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;
import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

/**
 * Created by bennj on 2017-07-19.
 */

public class ServerReader implements IDataUpdater {
    ApiWrapper mApi;

    public ServerReader(ApiWrapper api) {
        mApi = api;
    }

    @Override
    public void getValue() {

    }

    @Override
    public void update() {
        try {
            Currency c = mApi.getCurrency();
            Balance b = mApi.getBalance(c);
            DataMap.writeFloat(DataMapKey.BUY_VALUE, c.getBuy());
            DataMap.writeFloat(DataMapKey.SELL_VALUE, c.getSell());
            DataMap.writeFloat(DataMapKey.MIN_TRADE_UNIT, c.getMinTradeUnit());
            DataMap.writeFloat(DataMapKey.COIN_AMOUNT, b.getBitAmount());
            DataMap.writeFloat(DataMapKey.MONEY_VALUE_RAW, b.getRealMoney());


            Float totalValue = b.getBitAmount() +  b.getRealMoney();
            DataMap.writeFloat(DataMapKey.TOTAL_VALUE_RAW, totalValue);


        } catch (Exception e) {
            Log.e("ServerReader", e.getMessage());
            ShowErrorToast(e);
        }
    }
    protected void ShowErrorToast(Exception e) {
        DataMap.writeString(DataMapKey.ERROR_TOAST_CONTENT,e.getMessage());
    }

}
