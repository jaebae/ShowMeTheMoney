package com.sungjae.app.showmethemoney.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sungjae.app.showmethemoney.service.OperationService;

public class ServiceLaunchReceiver extends BroadcastReceiver {
    private final static String BOOT_COMPLETE_INTENT = "android.intent.action.BOOT_COMPLETED";
    private final static String LAUNCH_APP = "android.intent.action.LAUNCH_APP";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETE_INTENT) || intent.getAction().equals(LAUNCH_APP)) {
            startOperationService(context);
        }
    }

    private void startOperationService(Context context) {
        Intent intent = new Intent("OperationService");
        intent.setClassName(context, OperationService.class.getName());
        context.startService(intent);
    }

}
