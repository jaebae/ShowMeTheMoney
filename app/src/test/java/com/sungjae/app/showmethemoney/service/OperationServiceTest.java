package com.sungjae.app.showmethemoney.service;

import android.content.ContentResolver;
import android.content.Context;

import com.sungjae.app.showmethemoney.service.api.ApiWrapper;
import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;
import com.sungjae.app.showmethemoney.service.api.model.Result;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
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


    Currency mCurrency = spy(new Currency("ETC", 0f, 0f));
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

    float mRealMoney = 1000000.f;
    float mBitMoney = 100.f;

    @Test
    public void simulate() throws Exception {
        mRealMoney = 1000000.f;
        mBitMoney = 100.f;
        final float BASE_CURRENCY = 1000.f;

        float currency = BASE_CURRENCY;

        when(mApi.sell(anyFloat())).thenAnswer(new Answer<ArrayList<Result>>() {
            @Override
            public ArrayList<Result> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                float unit = (float) args[0];
                mRealMoney += unit * mCurrency.getBuy() * 0.9985f;
                mBitMoney -= unit;
                return null;
            }
        });

        when(mApi.buy(anyFloat())).thenAnswer(new Answer<ArrayList<Result>>() {
            @Override
            public ArrayList<Result> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                float unit = (float) args[0];
                mRealMoney -= unit * mCurrency.getSell();
                mBitMoney += unit * 0.9985f;
                return null;
            }
        });

        int i = 0;
        while (i < 360 * 40) {
            i += 3;
            currency = BASE_CURRENCY + (float) Math.sin(Math.toRadians(i)) * 500.f;

            when(mCurrency.getBuy()).thenReturn(currency);
            when(mCurrency.getSell()).thenReturn(currency);
            when(mBalance.getBitMoney()).thenReturn(mBitMoney);
            when(mBalance.getRealMoney()).thenReturn(mRealMoney);

            mOperationService.doOperation();
        }

        float total = mRealMoney + mBitMoney * currency;
        System.out.println("total = " + total);
    }
}
