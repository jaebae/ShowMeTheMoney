package com.sungjae.app.showmethemoney.service.api;

import com.sungjae.app.showmethemoney.service.api.model.Balance;
import com.sungjae.app.showmethemoney.service.api.model.Currency;
import com.sungjae.app.showmethemoney.service.api.model.Result;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;


public class ApiWrapper {
    private Api_Client mApi = new Api_Client("", "");
    private String mCoinType;

    public ApiWrapper(String coinType) {
        mCoinType = coinType;
    }

    public Currency getCurrency() throws Exception {
        String data = callApi("/public/ticker/", mCoinType, null);

        return new Currency(mCoinType,
                getFloatValue(data, "buy_price"),
                getFloatValue(data, "sell_price"),
                getFloatValue(data, "closing_price"));
    }

    public Balance getBalance(Currency currency) throws Exception {
        HashMap<String, String> param = new HashMap<>();
        param.put("currency", mCoinType);
        String data = callApi("/info/balance/", null, param);

        return new Balance(getFloatValue(data, "total_" + mCoinType.toLowerCase()),
                getFloatValue(data, "total_krw"),
                currency);
    }

    public ArrayList<Result> buy(float amount) throws Exception {
        HashMap<String, String> param = new HashMap<>();
        param.put("units", "" + amount);
        param.put("currency", mCoinType);
        String data = callApi("//trade/market_buy/", null, param);

        System.out.println(data);

        return toResult(data, "매입");
    }

    public ArrayList<Result> toResult(String data, String tradeType) {
        ArrayList<Result> resultList = new ArrayList<>();

        JSONArray jsonArray = getArrayValueObj(data);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            Result result = new Result(System.currentTimeMillis(),
                    tradeType,
                    getString("units", jsonObject),
                    getString("price", jsonObject),
                    getString("total", jsonObject)
            );

            resultList.add(result);
        }

        return resultList;
    }

    public ArrayList<Result> sell(float amount) throws Exception {
        HashMap<String, String> param = new HashMap<>();
        param.put("units", "" + amount);
        param.put("currency", mCoinType);
        String data = callApi("//trade/market_sell/", null, param);

        System.out.println(data);

        return toResult(data, "매도");
    }

    private String callApi(String api, String urlParam, HashMap<String, String> param) throws Exception {
        if (urlParam != null && !urlParam.isEmpty()) {
            api += urlParam;
        }

        return mApi.callApi(api, param);
    }


    private String getString(String key, JSONObject data) {
        String value = "";
        Object obj = data.get(key);
        if (obj instanceof String) {
            value = (String) obj;
        } else if (obj instanceof Long) {
            value = "" + (Long) obj;
        } else if (obj instanceof Integer) {
            value = "" + obj;
        }
        return value;
    }

    private JSONArray getArrayValueObj(String json) {
        JSONArray ret = null;
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
            ret = (JSONArray) jsonObj.get("data");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;

    }


    private String getValue(String json, String key) {
        String value = "";

        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
            JSONObject data = (JSONObject) jsonObj.get("data");
            value = getString(key, data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return value;
    }

    private float getFloatValue(String json, String key) {
        float ret = 0.0000f;
        String value = getValue(json, key);

        if (!value.isEmpty()) {
            ret = Float.parseFloat(value);
        }

        return ret;
    }
}
