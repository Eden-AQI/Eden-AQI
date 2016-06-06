package com.semc.aqi.module.main;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;
import com.semc.aqi.model.GankPic;
import com.semc.aqi.repository.json.GankListResult;

public class RankContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
    }
}
