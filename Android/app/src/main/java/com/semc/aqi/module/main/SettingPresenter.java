package com.semc.aqi.module.main;

import com.semc.aqi.model.AppUpdate;
import com.semc.aqi.repository.config.RetrofitManager;
import com.semc.aqi.repository.services.ConfigService;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingPresenter implements SettingContract.Presenter {

    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View view) {
        mView = view;
    }

    @Override
    public void checkUpdate() {
        ConfigService service = RetrofitManager.getRxRetrofit("http://1.yy317.sinaapp.com/").create(ConfigService.class);
        service.checkAppUpdate()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppUpdate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showCheckUpdateError(e);
                    }

                    @Override
                    public void onNext(AppUpdate appUpdate) {
                        mView.showCheckUpdateResult(appUpdate);
                    }
                });
    }
}
