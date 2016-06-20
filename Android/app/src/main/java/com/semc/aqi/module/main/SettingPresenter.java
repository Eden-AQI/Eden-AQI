package com.semc.aqi.module.main;

import com.semc.aqi.model.Update;
import com.semc.aqi.repository.WeatherRepository;

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
        WeatherRepository.getInstance().checkUpdate()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Update>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showCheckUpdateError(e);
                    }

                    @Override
                    public void onNext(Update update) {
                        mView.showCheckUpdateResult(update);
                    }
                });
    }
}
