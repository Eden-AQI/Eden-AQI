package com.semc.aqi.module.start;

public class StartPresenter implements StartContract.Presenter {

    private StartContract.View view;

    public StartPresenter(StartContract.View view) {
        this.view = view;
    }
}
