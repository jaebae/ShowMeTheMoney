package com.sungjae.app.showmethemoney.receiver;

import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceLaunchReceiverTest {
    @InjectMocks
    ServiceLaunchReceiver mServiceLaunchReceiver;

    @Mock
    Context mContext;

    @Mock
    Intent mIntent;


    @Test
    public void shouldStartServiceWhenBootCompleteIsReceived() throws Exception {
        when(mIntent.getAction()).thenReturn("NOT_ACTION");
        mServiceLaunchReceiver.onReceive(mContext, mIntent);
        verify(mContext, never()).startService(any(Intent.class));

        when(mIntent.getAction()).thenReturn("android.intent.action.BOOT_COMPLETED");
        mServiceLaunchReceiver.onReceive(mContext, mIntent);
        verify(mContext).startService(any(Intent.class));

        reset(mContext);
        when(mIntent.getAction()).thenReturn("android.intent.action.BOOT_COMPLETED");
        mServiceLaunchReceiver.onReceive(mContext, mIntent);
        verify(mContext).startService(any(Intent.class));
    }
}