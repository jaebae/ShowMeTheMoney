package com.sungjae.app.showmethemoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sungjae.com.app.showmethemoney.R;


public class MainActivity extends AppCompatActivity implements TradeViewInterface {
    private ListView mListView;
    private TradePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mPresenter = new TradePresenter(this, new TradeModel(this, getLoaderManager()));
        onCreateView();

        Intent intent = new Intent("android.intent.action.LAUNCH_APP");
        this.sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
        registerBroadcastReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcastReceivers();
    }

    @Override
    public void onCreateView() {
        mPresenter.onCreateView();
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    private void registerBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("UPDATE_VIEW");
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterBroadcastReceivers() {
        unregisterReceiver(mBroadcastReceiver);
    }

    float mBuy;
    float mSell;
    float mBitMoney;
    float mRealMoney;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBuy = (int) intent.getFloatExtra("buy", 0.f);
            mSell = (int) intent.getFloatExtra("sell", 0.f);
            mBitMoney = intent.getFloatExtra("bitMoney", 0.f);
            mRealMoney = (int) intent.getFloatExtra("realMoney", 0.f);

            updateView();

        }
    };

    private void updateView() {
        TextView tv = (TextView) findViewById(R.id.buy);
        tv.setText("매수가 : " + (int) mBuy);

        tv = (TextView) findViewById(R.id.sell);
        tv.setText("매도가 : " + (int) mSell);

        tv = (TextView) findViewById(R.id.bit_as_sell);
        float bitMoney = mBitMoney * mSell;
        float total = bitMoney + mRealMoney;
        float diff = mRealMoney - bitMoney;
        float percent = (diff / mRealMoney) * 100.f;
        tv.setText("매도가 기준 : " + (int) bitMoney + "(" + (int) total + ") " + (int) percent + "%");

        bitMoney = mBitMoney * mBuy;
        total = bitMoney + mRealMoney;
        diff = bitMoney - mRealMoney;
        percent = (diff / mRealMoney) * 100.f;
        tv = (TextView) findViewById(R.id.bit_as_buy);
        tv.setText("매수가 기준 : " + (int) bitMoney + "(" + (int) total + ") " + (int) percent + "%");

        tv = (TextView) findViewById(R.id.krw);
        tv.setText("현금 : " + mRealMoney);
    }
}
