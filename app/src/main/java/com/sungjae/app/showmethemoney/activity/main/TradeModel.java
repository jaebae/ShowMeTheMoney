package com.sungjae.app.showmethemoney.activity.main;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.sungjae.com.app.showmethemoney.R;

import java.util.ArrayList;

public class TradeModel implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context mContext;
    private LoaderManager mLoaderManager;

    private ArrayList<ModelDataListener> mDataListener = new ArrayList<>();

    private TradeListAdapter myAdapter;

    protected TradeModel() {
    }

    public TradeModel(Context context, LoaderManager loaderManager) {
        mContext = context;
        mLoaderManager = loaderManager;
        myAdapter = new TradeListAdapter(mContext, R.layout.list_item_layout, null, true);
    }

    public TradeListAdapter getListAdapter() {
        return myAdapter;
    }

    public void init() {
        mLoaderManager.initLoader(10, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String projection[] = new String[]{
                "date",
                "coin",
                "trade",
                "unit",
                "price",
                "amount",
                "_id"
        };

        return new CursorLoader(mContext, Uri.parse("content://trade/trade"), projection, null,
                null, "_id desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        myAdapter.changeCursor(cursor);
        for (ModelDataListener listener : mDataListener) {
            listener.onLoadFinished(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.changeCursor(null);
    }

    public void registerDataListener(ModelDataListener listener) {
        mDataListener.add(listener);
    }

    public void unRegisterDataListener(ModelDataListener listener) {
        mDataListener.remove(listener);
    }

    public void reload() {
        mLoaderManager.restartLoader(10, null, this);
    }

    public interface ModelDataListener {
        void onLoadFinished(Cursor cursor);
    }
}
