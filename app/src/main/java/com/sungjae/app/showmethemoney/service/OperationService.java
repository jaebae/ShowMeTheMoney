package com.sungjae.app.showmethemoney.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.sungjae.app.showmethemoney.activity.main.MainActivity;
import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.IDataUpdater;
import com.sungjae.app.showmethemoney.log.MyLog;
import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Result;
import com.sungjae.app.showmethemoney.trade.preProcessor.MoneyKeeper;
import com.sungjae.app.showmethemoney.trade.preProcessor.ServerReader;
import com.sungjae.app.showmethemoney.trade.rule.ITradeRule;
import com.sungjae.app.showmethemoney.trade.rule.TradeRuleFactory;
import com.sungjae.com.app.showmethemoney.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants.syncSettingsToDataMap;


public class OperationService extends Service {
    private static String COIN = ConfigurationConstants.getCurrency().toUpperCase();
    ApiWrapper mApi = new ApiWrapper(COIN);

    public OperationService() {
        super();
        MyLog.d(this, "OperationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        syncSettingsToDataMap();
        getHandler();

        MyLog.d(this, "onCreate");
    }


    protected Handler mHandler;

    private void updateCOIN() {
        String newCOIN = ConfigurationConstants.getCurrency().toUpperCase();
        if (newCOIN.equals(COIN) == false) {
            COIN = ConfigurationConstants.getCurrency().toUpperCase();
            mApi = new ApiWrapper(COIN);
        }
    }

    @NonNull
    private Handler getHandler() {
        mHandler = new Handler(getLooperHandlerThread()) {
            @Override
            public void handleMessage(Message msg) {
                removeMessages(0);
                updateCOIN();
                doOperation();
                sendEmptyMessageDelayed(0, 30000);
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
        IDataUpdater updaters[] = new IDataUpdater[]{new ServerReader(mApi), new MoneyKeeper()};

        boolean result = false;

        //get data from server
        //calculate data to more data
        for (IDataUpdater updater : updaters) {
            updater.getValue();
            result = updater.update();
            if (!result) {
                break;
            }
        }

        if (result == true) {
            // TODO: ADD balance currency to DB
            int currencyId = insertToCurrencyHistory();
            insertToBalanceHistory(currencyId);

            //do rules...
            ITradeRule trList[] = TradeRuleFactory.getRules();
            for (ITradeRule tr : trList) {
                if (tr.isEnabled()) {
                    tr.header();
                    tr.execute();
                    executeTrade();//do sell/buy
                    tr.footer();
                }
            }
        }

        //update GUI
        updateView();
        showNotification();

    }

    private void executeTrade() {
        try {
            ArrayList<Result> results = null;
            float unit = DataMap.readFloat(DataMap.TRADE_BUY_AMOUNT);
            DataMap.writeFloat(DataMap.TRADE_BUY_AMOUNT, 0f);
            if (unit != 0.0f) {
                results = mApi.buy(unit);
            }

            unit = DataMap.readFloat(DataMap.TRADE_SELL_AMOUNT);
            DataMap.writeFloat(DataMap.TRADE_SELL_AMOUNT, 0f);
            if (unit != 0.0f) {
                results = mApi.sell(unit);
            }

            if (results != null) {
                insertTradeDB(results);
            }
        } catch (Exception e) {
            ShowErrorToast(e);
        }
    }

    protected void ShowErrorToast(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void updateView() {
        Intent intent = new Intent("UPDATE_VIEW");
        intent.putExtra("buy", DataMap.readFloat(DataMap.BUY_VALUE));
        intent.putExtra("sell", DataMap.readFloat(DataMap.SELL_VALUE));
        intent.putExtra("bitMoney", DataMap.readFloat(DataMap.COIN_AMOUNT));
        intent.putExtra("realMoney", DataMap.readFloat(DataMap.MONEY_VALUE_RAW));
        intent.putExtra("realMoneyAvailable", DataMap.readFloat(DataMap.MONEY_VALUE_AVAIL));

        getApplicationContext().sendBroadcast(intent);
    }


    private ContentValues toContentValueCurrency() {
        ContentValues contentValue = new ContentValues();
        contentValue.put("date", System.currentTimeMillis());
        contentValue.put("coin", COIN);
        contentValue.put("sell", DataMap.readFloat(DataMap.SELL_VALUE));
        contentValue.put("buy", DataMap.readFloat(DataMap.BUY_VALUE));
        return contentValue;
    }


    private ContentValues toContentValue(int currencyId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("currency", currencyId);
        contentValue.put("realMoney", DataMap.readFloat(DataMap.MONEY_VALUE_RAW));
        contentValue.put("bitMoney", DataMap.readFloat(DataMap.COIN_AMOUNT));
        return contentValue;
    }

    private void insertToBalanceHistory(int currencyId) {
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/balance");

        ContentValues contentValue = toContentValue(currencyId);
        cr.insert(uri, contentValue);
        cr.notifyChange(uri, null);
    }


    private int insertToCurrencyHistory() {
        int ret = -1;
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/currency");
        ContentValues contentValue = toContentValueCurrency();
        Uri result = cr.insert(uri, contentValue);
        cr.notifyChange(uri, null);

        if (result != null) {
            ret = Integer.parseInt(result.getLastPathSegment());
        }

        return ret;
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyLog.d(this, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    private String createUpDownStatus() {
        StringBuilder out = new StringBuilder();
        float rate24, rate12, rate1;
        float old = readCurrencyHistory(24);
        rate24 = (DataMap.readFloat(DataMap.AVG_COIN_VALUE) - old) / old * 100;
        old = readCurrencyHistory(12);
        rate12 = (DataMap.readFloat(DataMap.AVG_COIN_VALUE) - old) / old * 100;
        old = readCurrencyHistory(1);
        rate1 = (DataMap.readFloat(DataMap.AVG_COIN_VALUE) - old) / old * 100;

        out.append(String.format("[%,.0f] ", DataMap.readFloat(DataMap.AVG_COIN_VALUE)));
        out.append("24H : " + String.format("%.1f", rate24) + "% ");
        out.append("12H : " + String.format("%.1f", rate12) + "% ");
        out.append("1H : " + String.format("%.1f", rate1) + "% ");
        return out.toString();
    }

    public void showNotification() {
        try {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            Intent Intent = new Intent(this, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    Intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            if (!TextUtils.isEmpty(DataMap.readString(DataMap.NOTIFICATION_CONTENT))) {
                mBuilder = createNotification("CUT OFF", DataMap.readString(DataMap.NOTIFICATION_CONTENT), Notification.DEFAULT_ALL);
                mNotificationManager.notify(2, mBuilder.build());
                DataMap.writeString(DataMap.NOTIFICATION_CONTENT,"");
            }

            if (!TextUtils.isEmpty(DataMap.readString(DataMap.ERROR_TOAST_CONTENT))) {
                mBuilder = createNotification("ERROR", DataMap.readString(DataMap.ERROR_TOAST_CONTENT), 0);
                //startForeground(1, mBuilder.build());
                Toast.makeText(this, DataMap.readString(DataMap.ERROR_TOAST_CONTENT), Toast.LENGTH_LONG).show();
                DataMap.writeString(DataMap.ERROR_TOAST_CONTENT, "");
            }
            else
            {
                mBuilder = createNotification("등락", createUpDownStatus(), 0);
                mBuilder.setContentIntent(resultPendingIntent);
            }

            startForeground(1, mBuilder.build());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private NotificationCompat.Builder createNotification(String title, String content, int alert) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.coin)
                .setContentTitle(title)
                .setContentTitle(content)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(alert)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        return builder;
    }

    private float readCurrencyHistory(int hourBefore) {
        float currency = 0f;
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/currency");
        long current = System.currentTimeMillis();
        long before = current - hourBefore * 60 * 60 * 1000;
        try (Cursor cursor = cr.query(uri, null, "date < ?", new String[]{Float.toString(before)}, "date desc limit 0,1")) {
            if (cursor.moveToFirst()) {
                before = cursor.getLong(cursor.getColumnIndex("date"));
                currency = (cursor.getFloat(cursor.getColumnIndex("sell")) + cursor.getFloat(cursor.getColumnIndex("buy"))) / 2;
            }
        }

        MyLog.d(this, "hour before=" + hourBefore + "  before=" + getLongToTime(this, before) + " / " + before + " currency=" + currency);
        return currency;
    }

    @NonNull
    private String getLongToTime(Context context, long date) {
        SimpleDateFormat dateFormat = (SimpleDateFormat) java.text.DateFormat
                .getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault());
        SimpleDateFormat timeFormat = (SimpleDateFormat) android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(date) + " \u200e" + timeFormat.format(date);
    }
}
