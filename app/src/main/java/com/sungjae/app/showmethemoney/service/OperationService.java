package com.sungjae.app.showmethemoney.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;
import com.sungjae.app.showmethemoney.service.api.model.Result;
import com.sungjae.app.showmethemoney.service.rule.TradeRule;
import com.sungjae.app.showmethemoney.service.rule.TradeRuleFactory;

import java.util.ArrayList;


public class OperationService extends Service {
    private final static String COIN = "ETH";
    ApiWrapper mApi = new ApiWrapper(COIN);

    public OperationService() {
        super();
        Log.d("OperationService", "OperationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startOperationThread();
        Log.d("OperationService", "onCreate");
    }

    protected void startOperationThread() {
        OperationThread operationThread = new OperationThread(getHandler());
        operationThread.start();
    }

    Handler mHandler;

    @NonNull
    private Handler getHandler() {
        mHandler = new Handler(getLooperHandlerThread()) {
            @Override
            public void handleMessage(Message msg) {
                doOperation();
            }
        };
        return mHandler;
    }

    @NonNull
    private Looper getLooperHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("BG_THREAD");
        handlerThread.start();
        return handlerThread.getLooper();
    }


    protected void doOperation() {
        try {
            Currency c = mApi.getCurrency();
            Balance b = mApi.getBalance(c);

            int currencyId = insertToCurrencyHistory(c);
            insertToBalanceHistory(b, currencyId);

            ArrayList<Result> results = null;

            TradeRule tr = TradeRuleFactory.getRule(c,b);

            float unit = tr.getBuyDiff();
            if (unit != 0.0f) {
                results = mApi.buy(unit);
            }

            unit = tr.getSellDiff();
            if (unit != 0.0f) {
                results = mApi.sell(unit);
            }

            if (results != null) {
                insertTradeDB(results);
            }

            updateView(c, b);

        } catch (Exception e) {
            Log.e("OperationSvc", e.getMessage());
            ShowErrorToast(e);
        }
    }

    protected void ShowErrorToast(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void updateView(Currency c, Balance b) {
        Intent intent = new Intent("UPDATE_VIEW");
        intent.putExtra("buy", c.getBuy());
        intent.putExtra("sell", c.getSell());
        intent.putExtra("bitMoney", b.getBitMoney());
        intent.putExtra("realMoney", b.getRealMoney());

        getApplicationContext().sendBroadcast(intent);
    }


    private void insertToBalanceHistory(Balance balance, int currencyId) {
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/balance");

        ContentValues contentValue = toContentValue(balance, currencyId);
        cr.insert(uri, contentValue);
        cr.notifyChange(uri, null);
    }


    private int insertToCurrencyHistory(Currency currency) {
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/currency");
        ContentValues contentValue = toContentValue(currency);
        Uri result = cr.insert(uri, contentValue);
        cr.notifyChange(uri, null);
        return Integer.parseInt(result.getLastPathSegment());
    }


    protected void insertTradeDB(ArrayList<Result> results) {
        ContentResolver cr = getContentResolver();
        Uri uri = Uri.parse("content://trade/trade");

        for (Result result : results) {
            ContentValues contentValue = toContentValue(result);
            cr.insert(uri, contentValue);
            cr.notifyChange(uri, null);
        }
    }

    private ContentValues toContentValue(Result result) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("date", result.mDate);
        contentValue.put("coin", COIN);
        contentValue.put("trade", result.mTrade);
        contentValue.put("unit", result.mUnit);
        contentValue.put("price", result.mPrice);
        contentValue.put("amount", result.mAmount);
        return contentValue;
    }

    private ContentValues toContentValue(Currency currency) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("date", System.currentTimeMillis());
        contentValue.put("coin", COIN);
        contentValue.put("sell", currency.getSell());
        contentValue.put("buy", currency.getBuy());
        return contentValue;
    }

    private ContentValues toContentValue(Balance balance, int currencyId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("currency", currencyId);
        contentValue.put("realMoney", balance.getRealMoney());
        contentValue.put("bitMoney", balance.getBitMoney());
        return contentValue;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("OperationService", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }
}
