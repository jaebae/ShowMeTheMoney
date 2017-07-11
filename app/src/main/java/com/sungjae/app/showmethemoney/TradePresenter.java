package com.sungjae.app.showmethemoney;


public class TradePresenter {
    private TradeViewInterface mView;
    private TradeModel mModel;

    public TradePresenter(TradeViewInterface view, TradeModel model) {
        mView = view;
        mModel = model;
    }

    protected void setAdapter() {
        mView.setListAdapter(mModel.getListAdapter());
    }

    public void onCreateView() {
        mModel.init();

        setAdapter();
    }
}
