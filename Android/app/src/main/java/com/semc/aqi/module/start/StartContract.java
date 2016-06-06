package com.semc.aqi.module.start;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;
import com.semc.aqi.model.IpInfo;
import com.semc.aqi.repository.json.Result;

public class StartContract {
    interface Presenter extends BasePresenter {
        void getIpInfo(String ip);
    }

    interface View extends BaseView<Presenter> {
        void gotoMainActivity(Result<IpInfo> ipInfoResult);
        void showLoadError(Throwable e);
    }
}
