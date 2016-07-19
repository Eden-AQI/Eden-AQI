package com.semc.aqi.module.start;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;

public class StartContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
        void gotoMainActivity();
    }
}
