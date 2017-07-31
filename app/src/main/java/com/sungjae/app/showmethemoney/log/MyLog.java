package com.sungjae.app.showmethemoney.log;


import android.util.Log;

public class MyLog {
    private final static String TAG = "SMTM";

    private static int mLogIndex = 0;
    private static int MAX_LOG_INDEX = 9999;

    private MyLog() {
    }

    private static int getLogIndex() {
        mLogIndex++;
        if (mLogIndex > MAX_LOG_INDEX) {
            mLogIndex = 0;
        }
        return mLogIndex;
    }

    private static String getMsg(Object obj, String msg) {
        return String.format("[%04d/%-20s] %s", getLogIndex(), obj.getClass().getSimpleName(), msg);
    }

    public static void d(Object obj, String msg) {
        Log.d(TAG, getMsg(obj, msg));
    }

    public static void e(Object obj, String msg) {
        Log.e(TAG, getMsg(obj, msg));
    }
}
