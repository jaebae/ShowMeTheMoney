package com.sungjae.app.showmethemoney.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.app.showmethemoney.data.DataMapKey;
import com.sungjae.app.showmethemoney.data.IDataUpdater;
import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;
import com.sungjae.app.showmethemoney.service.api.model.Result;
import com.sungjae.app.showmethemoney.trade.preProcessor.MoneyKeeper;
import com.sungjae.app.showmethemoney.trade.preProcessor.ServerReader;
import com.sungjae.app.showmethemoney.trade.rule.ITradeRule;
import com.sungjae.app.showmethemoney.trade.rule.TradeRuleFactory;
import com.sungjae.com.app.showmethemoney.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


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


    protected void doOperation()
    {
        IDataUpdater updaters[] = new IDataUpdater[] {new ServerReader(mApi),new MoneyKeeper() };

        //get data from server
        //calculate data to more data
        for (IDataUpdater updater:updaters) {
            updater.getValue();
            updater.update();
        }
        // TODO: ADD balance currency to DB
        int currencyId = insertToCurrencyHistory();
        insertToBalanceHistory(currencyId);

        //do rules...
        ITradeRule trList[] = TradeRuleFactory.getRules();
        for(ITradeRule tr : trList)
        {
            if(tr.isEnabled()) {
                tr.header();
                tr.execute();
                executeTrade();//do sell/buy
                tr.footer();

            }
        }


        //update GUI
        updateView();
        showNotification();

    }

    private void executeTrade()
    {
        try {
            ArrayList<Result> results = null;
            float unit = DataMap.readFloat(DataMapKey.TRADE_BUY_AMOUNT);
            DataMap.writeFloat(DataMapKey.TRADE_BUY_AMOUNT,0f);
            if (unit != 0.0f) {
                    results = mApi.buy(unit);

            }

            unit = DataMap.readFloat(DataMapKey.TRADE_SELL_AMOUNT);
            DataMap.writeFloat(DataMapKey.TRADE_SELL_AMOUNT,0f);
            if (unit != 0.0f) {
                results = mApi.sell(unit);
            }

            if (results != null) {
                insertTradeDB(results);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    protected void doOperation() {
//        try {
//            Currency c = mApi.getCurrency();
//            Balance b = mApi.getBalance(c);
//
//            int currencyId = insertToCurrencyHistory(c);
//            insertToBalanceHistory(b, currencyId);
//
//            ArrayList<Result> results = null;
//
//            ITradeRule tr = TradeRuleFactory.getRules();
//
//            float unit = tr.getBuyAmount();
//            if (unit != 0.0f) {
//                results = mApi.buy(unit);
//            }
//
//            unit = tr.getSellAmount();
//            if (unit != 0.0f) {
//                results = mApi.sell(unit);
//            }
//
//            if (results != null) {
//                insertTradeDB(results);
//            }
//
//            updateView(c, b);
//
//        } catch (Exception e) {
//            Log.e("OperationSvc", e.getMessage());
//            ShowErrorToast(e);
//        }
//    }

    protected void ShowErrorToast(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void updateView() {
        Intent intent = new Intent("UPDATE_VIEW");
        intent.putExtra("buy", DataMap.readFloat(DataMapKey.BUY_VALUE));
        intent.putExtra("sell", DataMap.readFloat(DataMapKey.SELL_VALUE));
        intent.putExtra("bitMoney", DataMap.readFloat(DataMapKey.COIN_AMOUNT));
        intent.putExtra("realMoney", DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW));
        intent.putExtra("realMoneyAvailable", DataMap.readFloat(DataMapKey.MONEY_VALUE_AVAIL));

        getApplicationContext().sendBroadcast(intent);
    }




    private ContentValues toContentValueCurrency() {
        ContentValues contentValue = new ContentValues();
        contentValue.put("date", System.currentTimeMillis());
        contentValue.put("coin", COIN);
        contentValue.put("sell", DataMap.readFloat(DataMapKey.SELL_VALUE));
        contentValue.put("buy", DataMap.readFloat(DataMapKey.BUY_VALUE));
        return contentValue;
    }



    private ContentValues toContentValue( int currencyId) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("currency", currencyId);
        contentValue.put("realMoney", DataMap.readFloat(DataMapKey.MONEY_VALUE_RAW));
        contentValue.put("bitMoney", DataMap.readFloat(DataMapKey.COIN_AMOUNT));
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
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://trade/currency");
        ContentValues contentValue = toContentValueCurrency();
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

    private String createUpDownStatus()
    {
        StringBuilder out = new StringBuilder();
        float rate24,rate12,rate1;
        float old = readCurrencyHistory(24);
        rate24 = (DataMap.readFloat(DataMapKey.AVG_COIN_VALUE)-old )/old*100;
        old = readCurrencyHistory(12);
        rate12 = (DataMap.readFloat(DataMapKey.AVG_COIN_VALUE)-old )/old*100;
        old = readCurrencyHistory(1);
        rate1 = ( DataMap.readFloat(DataMapKey.AVG_COIN_VALUE)-old )/old*100;

        out.append("["+COIN+"="+ String.format("%.0f",DataMap.readFloat(DataMapKey.AVG_COIN_VALUE))+"] ");
        out.append("24H : "+String.format("%.1f",rate24)+"% ");
        out.append("12H : "+String.format("%.1f",rate12)+"% ");
        out.append("1H : "+String.format("%.1f",rate1)+"% ");
        return out.toString();
    }

    public void showNotification() {
        try {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;

            mBuilder = createNotification("등락",createUpDownStatus(),0);
            startForeground(1, mBuilder.build());
            if(DataMap.readString(DataMapKey.NOTIFICATION_CONTENT).isEmpty()==false) {
                mBuilder = createNotification("CUT OFF",DataMap.readString(DataMapKey.NOTIFICATION_CONTENT),Notification.DEFAULT_ALL);
                mNotificationManager.notify(2, mBuilder.build());
            }
        } catch (Exception e)
        {

        }

    }
    private NotificationCompat.Builder createNotification(String title, String content,int alert ){
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.coin);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.coin)
            //    .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentTitle(content)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(alert);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        return builder;
    }

private float readCurrencyHistory(int hourBefore)
{
    float currency = 0f;

    ContentResolver cr = getContentResolver();

    Uri uri = Uri.parse("content://trade/currency");
    long current = System.currentTimeMillis();
    long before = current - hourBefore*60*60*1000;
    Cursor cursor = cr.query(uri, null, "date < ?", new String[] {Float.toString(before)},"date desc limit 0,1");
    cursor.moveToFirst();
    before = cursor.getLong(cursor.getColumnIndex("date"));
    currency =    ( cursor.getFloat(cursor.getColumnIndex("sell"))+cursor.getFloat(cursor.getColumnIndex("buy")))/2;

    Log.d("SMTM","hour before="+hourBefore+"  before="+getLongToTime(this,before) +" / "+before+" currency="+currency );
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
