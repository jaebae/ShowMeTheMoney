package com.sungjae.app.showmethemoney;



import android.content.Context;
import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TradeListAdapterTest {

    TradeListAdapter mTradeListAdapter;

    @Mock
    Context mContext;

    @Mock
    Cursor mCursor;

    @Before
    public void setUp() throws Exception {
        mTradeListAdapter = new TradeListAdapter(mContext, 0, mCursor, true);
    }

    @Test
    public void trimNumberTest() throws Exception {
        String in = "1234.123123123";
        String out = mTradeListAdapter.trimNumber(in);

        assertThat(out, is("1234.12"));

        in = "1234.1";
        out = mTradeListAdapter.trimNumber(in);
        assertThat(out, is("1234.1"));

        in = "1234.12";
        out = mTradeListAdapter.trimNumber(in);
        assertThat(out, is("1234.12"));

        in = "1234.123";
        out = mTradeListAdapter.trimNumber(in);
        assertThat(out, is("1234.12"));

        in = "1234";
        out = mTradeListAdapter.trimNumber(in);
        assertThat(out, is("1234"));

    }
}