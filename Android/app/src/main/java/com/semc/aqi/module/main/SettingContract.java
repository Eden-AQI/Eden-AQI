package com.semc.aqi.module.main;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;
import com.semc.aqi.model.Update;

public class SettingContract {
    interface Presenter extends BasePresenter {
        void checkUpdate();
    }

    interface View extends BaseView<Presenter> {
        void showCheckUpdateResult(Update update);
        void showCheckUpdateError(Throwable e);
    }
}
