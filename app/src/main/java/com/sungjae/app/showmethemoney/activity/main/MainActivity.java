package com.sungjae.app.showmethemoney.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.activity.setting.SettingFragment;
import com.sungjae.app.showmethemoney.activity.setting.SettingsActivity;
import com.sungjae.app.showmethemoney.data.DataMap;
import com.sungjae.com.app.showmethemoney.R;

import static com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants.syncSettingsToDataMap;


public class MainActivity extends AppCompatActivity implements TradeViewInterface {
    private ListView mListView;
    private TradePresenter mPresenter;
    CurrencyGraph graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigurationConstants.init(this);

        if (savedInstanceState != null) {
            mBuy = savedInstanceState.getFloat("buy");
            mSell = savedInstanceState.getFloat("sell");
            mBitMoney = savedInstanceState.getFloat("bit_money");
            mRealMoney = savedInstanceState.getFloat("real_money");
            mAvailMoney = savedInstanceState.getFloat("avail_money");
        }

        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list);
        mPresenter = new TradePresenter(this, new TradeModel(this, getLoaderManager()));
        onCreateView();


        registerBroadcastReceivers();
        Intent intent = new Intent("android.intent.action.LAUNCH_APP");
        this.sendBroadcast(intent);
        graph = new CurrencyGraph(this, (GraphView) findViewById(R.id.graph));

    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceivers();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncSettingsToDataMap();
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
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
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

    private float mBuy;
    private float mSell;
    private float mBitMoney;
    private float mRealMoney;
    private float mAvailMoney;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBuy = (int) intent.getFloatExtra("buy", 0.f);
            mSell = (int) intent.getFloatExtra("sell", 0.f);
            mBitMoney = intent.getFloatExtra("bitMoney", 0.f);
            mRealMoney = (int) intent.getFloatExtra("realMoney", 0.f);
            mAvailMoney = (int) intent.getFloatExtra("realMoneyAvailable", 0.f);

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
        outState.putFloat("avail_money", mAvailMoney);
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
        float total = bitMoney + mAvailMoney;

        float investRate = DataMap.readFloat(SettingFragment.SETTING_HEADER + ConfigurationConstants.INVEST_RATE);
        float expectedCoinValue = total * investRate;
        float diff = expectedCoinValue - bitMoney;
        float percent = (diff / Math.min(mAvailMoney, bitMoney)) * 100.f;

        tv.setText("매도가 기준 : " + mBitMoney + " / " + (int) bitMoney + " (" + (int) total + ") " + String.format("%.1f", percent) + "%");
        if (percent < 0) {
            tv.setTextColor(Color.RED);
        } else {
            tv.setTextColor(Color.BLUE);
        }
        bitMoney = mBitMoney * mBuy;
        total = bitMoney + mAvailMoney;
        diff = expectedCoinValue - bitMoney;
        percent = (diff / Math.min(mAvailMoney, bitMoney)) * 100.f;
        float totalInput = ConfigurationConstants.getTotalInput();
        float earnRate = (bitMoney + mRealMoney) / totalInput * 100 - 100;

        ((TextView) findViewById(R.id.totalInput)).setText("총 입금액 : " + (int) totalInput);
        ((TextView) findViewById(R.id.earnRate)).setText("이익률 : " + String.format("%.1f", earnRate) + "%");

        tv = (TextView) findViewById(R.id.bit_as_buy);
        tv.setText("매수가 기준 : " + mBitMoney + " / " + (int) bitMoney + " (" + (int) total + ") " + String.format("%.1f", percent) + "%");
        if (percent < 0) {
            tv.setTextColor(Color.RED);
        } else {
            tv.setTextColor(Color.BLUE);
        }
//        tv = (TextView) findViewById(R.id.krw);
//        tv.setText("가용 현금 : " + (int) mAvailMoney+" / 총 현금 : " +(int) mRealMoney+" / 보관금 : " +(int) (mRealMoney-mAvailMoney));

        ((TextView) findViewById(R.id.krw)).setText("가용 현금 : " + (int) mAvailMoney);
        ((TextView) findViewById(R.id.krw3)).setText("보관금 : " + (int) (mRealMoney - mAvailMoney));
        ((TextView) findViewById(R.id.krw2)).setText("총 현금 : " + (int) mRealMoney);
        mPresenter.reloadList();

        graph.add(( mBuy+mSell)/2) ;
    }
}
