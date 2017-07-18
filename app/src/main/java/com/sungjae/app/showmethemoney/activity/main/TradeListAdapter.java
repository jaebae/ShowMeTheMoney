package com.sungjae.app.showmethemoney.activity.main;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.sungjae.com.app.showmethemoney.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class TradeListAdapter extends ResourceCursorAdapter {


    public TradeListAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    public TradeListAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateView = (TextView) view.findViewById(R.id.date);
        TextView typeView = (TextView) view.findViewById(R.id.type);
        TextView tradeView = (TextView) view.findViewById(R.id.trade);
        TextView unitView = (TextView) view.findViewById(R.id.unit);
        TextView priceView = (TextView) view.findViewById(R.id.price);
        TextView amountView = (TextView) view.findViewById(R.id.amount);

        dateView.setText(getLongToTime(context, cursor.getLong(0)));
        typeView.setText(cursor.getString(1));
        tradeView.setText(cursor.getString(2));
        //unitView.setText(trimNumber(cursor.getString(3)));
        unitView.setText(""+cursor.getFloat(3));
        priceView.setText(""+cursor.getFloat(4));
        amountView.setText(""+cursor.getFloat(5));
    }

    @NonNull
    private String getLongToTime(Context context, long date) {
        SimpleDateFormat dateFormat = (SimpleDateFormat) java.text.DateFormat
                .getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault());
        SimpleDateFormat timeFormat = (SimpleDateFormat) DateFormat.getTimeFormat(context);
        return dateFormat.format(date) + " \u200e" + timeFormat.format(date);
    }

    public String trimNumber(String str) {
        int pointer = str.lastIndexOf('.');
        if (pointer > 0 && pointer + 3 < str.length()) {
            str = str.substring(0, pointer + 3);
        }

        return str;

    }


}
