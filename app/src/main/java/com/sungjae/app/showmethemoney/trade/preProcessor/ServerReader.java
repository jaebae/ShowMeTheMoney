package com.sungjae.app.showmethemoney.trade.preProcessor;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.IDataUpdater;
import com.sungjae.app.showmethemoney.log.MyLog;
import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

import org.apache.commons.lang3.StringEscapeUtils;


public class ServerReader implements IDataUpdater {
    private ApiWrapper mApi;

    public ServerReader(ApiWrapper api) {
        mApi = api;
    }

    @Override
    public void getValue() {

    }

    @Override
    public boolean update() {
        try {
            Currency c = mApi.getCurrency();
            Balance b = mApi.getBalance(c);
            DataMap.writeFloat(DataMap.BUY_VALUE, c.getBuy());
            DataMap.writeFloat(DataMap.SELL_VALUE, c.getSell());
            DataMap.writeFloat(DataMap.MIN_TRADE_UNIT, c.getMinTradeUnit());
            DataMap.writeFloat(DataMap.COIN_AMOUNT, b.getBitAmount());
            DataMap.writeFloat(DataMap.MONEY_VALUE_RAW, b.getRealMoney());


            Float totalValue = b.getBitAmount() + b.getRealMoney();
            DataMap.writeFloat(DataMap.TOTAL_VALUE_RAW, totalValue);

            float avg = (DataMap.readFloat(DataMap.BUY_VALUE) + DataMap.readFloat(DataMap.SELL_VALUE)) / 2;
            DataMap.writeFloat(DataMap.AVG_COIN_VALUE, avg);

        } catch (Exception e) {
            String msg = StringEscapeUtils.unescapeJava(e.getMessage());
            MyLog.e(this, msg);
            ShowErrorToast(msg);
            return false;
        }
        return true;
    }

    protected void ShowErrorToast(String msg ) {
        DataMap.writeString(DataMap.ERROR_TOAST_CONTENT, msg);
    }

}
