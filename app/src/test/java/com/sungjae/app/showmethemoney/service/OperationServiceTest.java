package com.sungjae.app.showmethemoney.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {
    @InjectMocks
    @Spy
    OperationService mOperationService;


    @Test
    public void threadShouldRunOnCreate() throws Exception {
        mOperationService.onCreate();
        verify(mOperationService).startOperationThread();
    }
}