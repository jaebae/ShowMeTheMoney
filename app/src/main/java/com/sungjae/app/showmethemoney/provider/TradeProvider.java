package com.sungjae.app.showmethemoney.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TradeProvider extends ContentProvider {
    private static final String DATABASE_NAME = "trade.db";
    private static final int DATABASE_VERSION = 209;
    private static final String TABLE_NAME = "TRADE";

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        TradeDbHelper helper = new TradeDbHelper(getContext());
        mDatabase = helper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        return mDatabase.query(TABLE_NAME, projectionIn, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri ret = null;
        long rowId = mDatabase.insert(TABLE_NAME, null, contentValues);

        if (rowId > 0) {
            ret = ContentUris.withAppendedId(uri, rowId);
        }

        return ret;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


    private static class TradeDbHelper extends SQLiteOpenHelper {

        public TradeDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(getDropTableQuery());
            db.execSQL(getCreateTableQuery());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private String getCreateTableQuery() {
            return "CREATE TABLE IF NOT EXISTS TRADE " +
                    "( _id integer PRIMARY KEY, " +
                    " date long, " +
                    " coin text, " +
                    " trade text, " +
                    " unit text, " +
                    " price text, " +
                    " amount text )";
        }

        private String getDropTableQuery() {
            return "DROP TABLE IF EXISTS TRADE";
        }

    }
}
