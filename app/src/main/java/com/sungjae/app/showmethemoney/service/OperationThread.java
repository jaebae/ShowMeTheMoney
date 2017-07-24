package com.sungjae.app.showmethemoney.service;


import android.os.Handler;

public class OperationThread extends Thread {
    private Handler mHandler;

    public OperationThread(Handler handler) {
        mHandler = handler;
    }

//    @Override
//    public void run() {
//        while (true) {
////            mHandler.sendEmptyMessageDelayed(0, 30000);
////            try {
////                Thread.sleep(30000);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }
//    }
}
