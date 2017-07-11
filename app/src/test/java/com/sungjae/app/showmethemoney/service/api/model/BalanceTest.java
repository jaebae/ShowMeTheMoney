package com.sungjae.app.showmethemoney.service.api.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BalanceTest {

    @Test
    public void sellDiffTest() throws Exception {

        Balance balance = new Balance(106.f, 100.f, new Currency("ETH", 1.f, 1.f, 0.f));
        assertThat(balance.getSellDiff(), is(3.f));

        balance = new Balance(1060.f, 100.f, new Currency("ETH", 0.1f, 1.f, 0.f));
        assertThat(balance.getSellDiff(), is(30.f));
    }


    @Test
    public void buyDiffTest() throws Exception {
        Balance balance = new Balance(100.f, 106.f, new Currency("ETH", 1.f, 1.f, 0.f));
        assertThat(balance.getBuyDiff(), is(3.f));

        balance = new Balance(1000.f, 106.f, new Currency("ETH", 1.f, 0.1f, 0.f));
        assertThat(balance.getBuyDiff(), is(30.f));
    }
}