package com.sungjae.app.showmethemoney.activity.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sungjae.app.showmethemoney.activity.setting.ConfigurationConstants;
import com.sungjae.app.showmethemoney.log.MyLog;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by bennj on 2017-08-23.
 * REFER : http://www.android-graphview.org/
 */

public class CurrencyGraph {
    GraphView graph;
    Context ctx;
    ArrayList<DataPoint> dps = new ArrayList<>();
    long idx=0;
    LineGraphSeries<DataPoint> series;
    final static int MAX = 100;

    public CurrencyGraph(Context ctx,GraphView graph) {
        this.ctx=ctx;
        //GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(readCurrencyHistory());
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(MAX);
    }

    public void add(float currency)
    {
        MyLog.d(ctx,"GRAPH_ADD = "+currency);
        if(currency==0) return;
        series.appendData(new DataPoint(idx++,currency),true,MAX);
    }

    private DataPoint[] readCurrencyHistory() {
        float currency = 0f;
        ContentResolver cr = ctx.getContentResolver();

        Uri uri = Uri.parse("content://trade/currency");
        try (Cursor cursor = cr.query(uri, null, "coin = ?", new String[]{ConfigurationConstants.getCurrency()}, "date asc limit 0,"+MAX)) {
            if (cursor.moveToFirst()) {

                while (cursor.isLast() == false) {
                    //before = cursor.getLong(cursor.getColumnIndex("date"));
                    currency = (cursor.getFloat(cursor.getColumnIndex("sell")) + cursor.getFloat(cursor.getColumnIndex("buy"))) / 2;
                    if(currency==0) continue;
                    dps.add(new DataPoint(idx++, currency));
                    cursor.moveToNext();
                }
            }
        }

        //MyLog.d(this, "hour before=" + hourBefore + "  before=" + getLongToTime(this, before) + " / " + before + " currency=" + currency);
        DataPoint[] dpa = new DataPoint[dps.size()];
        dpa = dps.toArray(dpa);
        return dpa;
    }
}
