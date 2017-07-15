package com.sungjae.app.showmethemoney.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingsActiviy;
import com.sungjae.com.app.showmethemoney.R;


public class MainActivity extends AppCompatActivity implements TradeViewInterface {
    private ListView mListView;
    private TradePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigurationConstants.init(this);

        if (savedInstanceState != null) {
            mBuy = savedInstanceState.getFloat("buy");
            mSell = savedInstanceState.getFloat("sell");
            mBitMoney = savedInstanceState.getFloat("bit_money");
            mRealMoney = savedInstanceState.getFloat("real_money");
        }

        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mPresenter = new TradePresenter(this, new TradeModel(this, getLoaderManager()));
        onCreateView();


        registerBroadcastReceivers();
        Intent intent = new Intent("android.intent.action.LAUNCH_APP");
        this.sendBroadcast(intent);

    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceivers();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(getApplicationContext(), SettingsActiviy.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("buy", mBuy);
        outState.putFloat("sell", mSell);
        outState.putFloat("bit_money", mBitMoney);
        outState.putFloat("real_money", mRealMoney);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

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
        tv.setText("매도가 기준 : " + mBitMoney + " / " + (int) bitMoney + " (" + (int) total + ") " + String.format("%.1f", percent) + "%");

        bitMoney = mBitMoney * mBuy;
        total = bitMoney + mRealMoney;
        diff = bitMoney - mRealMoney;
        percent = (diff / mRealMoney) * 100.f;
        tv = (TextView) findViewById(R.id.bit_as_buy);
        tv.setText("매수가 기준 : " + mBitMoney + " / " + (int) bitMoney + " (" + (int) total + ") " + String.format("%.1f", percent) + "%");

        tv = (TextView) findViewById(R.id.krw);
        tv.setText("현금 : " + (int) mRealMoney);

        mPresenter.reloadList();
    }
}
