package com.semc.aqi.module.main;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;
import com.semc.aqi.model.AppUpdate;

public class SettingContract {
    interface Presenter extends BasePresenter {
        void checkUpdate();
    }

    interface View extends BaseView<Presenter> {
        void showCheckUpdateResult(AppUpdate appUpdate);
        void showCheckUpdateError(Throwable e);
    }
}
