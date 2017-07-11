package com.sungjae.app.showmethemoney.service;

import android.content.ContentResolver;
import android.content.Context;

import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {
    @InjectMocks
    @Spy
    OperationService mOperationService;

    @Mock
    ApiWrapper mApi;


    Currency mCurrency = spy(new Currency("BTC", 0f, 0f, 0f));
    Balance mBalance = spy(new Balance(0f, 0f, mCurrency));


    @Before
    public void setUp() throws Exception {
        when(mApi.getBalance(any(Currency.class))).thenReturn(mBalance);
        when(mApi.getCurrency()).thenReturn(mCurrency);
        when(mOperationService.getContentResolver()).thenReturn(mock(ContentResolver.class));
        when(mOperationService.getApplicationContext()).thenReturn(mock(Context.class));
        doNothing().when(mOperationService).ShowErrorToast(any(Exception.class));
    }

    @Test
    public void threadShouldRunOnCreate() throws Exception {
        mOperationService.onCreate();
        verify(mOperationService).startOperationThread();
    }

    @Test
    public void simulate() throws Exception {

        float bitMoney = 100.f;
        float realMoney = 1000000.f;

        when(mBalance.getBitMoney()).thenReturn(bitMoney);
        when(mBalance.getRealMoney()).thenReturn(realMoney);
        mOperationService.doOperation();
    }
}