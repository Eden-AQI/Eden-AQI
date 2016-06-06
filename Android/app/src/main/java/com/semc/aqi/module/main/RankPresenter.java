package com.semc.aqi.module.main;

import com.semc.aqi.model.GankPic;
import com.semc.aqi.repository.config.RetrofitManager;
import com.semc.aqi.repository.json.GankListResult;
import com.semc.aqi.repository.services.GankService;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RankPresenter implements RankContract.Presenter {

    private RankContract.View mView;

    public RankPresenter(RankContract.View view) {
        mView = view;
    }
}
