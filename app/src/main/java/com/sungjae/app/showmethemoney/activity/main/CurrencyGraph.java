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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(MAX-1);
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);
        this.graph=graph;
    }

    public void refresh()
    {
        idx=0;
        series.resetData(readCurrencyHistory());

    }
    private String getTime() {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date());
    }
    private String getTime(Long date) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }
    public void add(float currency)
    {
        MyLog.d(ctx,"GRAPH_ADD = "+currency);
        if(currency==0) return;
        if(series!=null)
            series.appendData(new DataPoint(idx++,currency),true,MAX);
    }

    private DataPoint[] readCurrencyHistory() {
        float currency = 0f;
        ContentResolver cr = ctx.getContentResolver();
        dps.clear();

        Uri uri = Uri.parse("content://trade/currency");
        try (Cursor cursor = cr.query(uri, null, "coin = ?", new String[]{ConfigurationConstants.getCurrency()}, "date desc limit 0,"+MAX)) {
            if (cursor.moveToLast()) {

                while (cursor.isBeforeFirst() == false) {
                    Long before = cursor.getLong(cursor.getColumnIndex("date"));
                    currency = (cursor.getFloat(cursor.getColumnIndex("sell")) + cursor.getFloat(cursor.getColumnIndex("buy"))) / 2;
                    if(currency==0) continue;
                    MyLog.d(ctx,"GRAPH_INIT = ["+getTime(before)+"/"+idx+"] "+currency);
                    dps.add(new DataPoint(idx++, currency));
                    cursor.moveToPrevious();
                }
            }
        }

        //MyLog.d(this, "hour before=" + hourBefore + "  before=" + getLongToTime(this, before) + " / " + before + " currency=" + currency);
        DataPoint[] dpa = new DataPoint[dps.size()];
        dpa = dps.toArray(dpa);
        return dpa;
    }
}
