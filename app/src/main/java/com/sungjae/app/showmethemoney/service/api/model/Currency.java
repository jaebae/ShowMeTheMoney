package com.sungjae.app.showmethemoney.service.api.model;

import java.util.HashMap;

public class Currency {
	float mBuy;
	float mSell;
	float mLatest;
	String mCoinType;
	
	HashMap <String, Float>mMinTrade = new HashMap<>();

	public Currency(String coinType, float buy, float sell, float latest) {
		mCoinType = coinType;
		mBuy = buy;
		mSell = sell;
		mLatest = latest;
		
		mMinTrade.put("BTC", 10000.0f );
		mMinTrade.put("ETH", 1000.0f );
		mMinTrade.put("DASH", 1000.0f );
		mMinTrade.put("LTC", 100.0f );
		mMinTrade.put("ETC", 100.0f );
		mMinTrade.put("XRP", 1.0f );
	}



	@Override
	public String toString() {
		return "Buy = " + mBuy + "\nSell = " + mSell + "\nLatest = " + mLatest;
	}

	public String getCoinType() {
		return mCoinType;
	}
	
	public float getMinTradeRate() {
		return mMinTrade.get(mCoinType);
	}

	public float getBuy() {
		return mBuy;
	}

	public float getSell() {
		return mSell;
	}

}