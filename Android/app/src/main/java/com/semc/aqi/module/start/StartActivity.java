package com.semc.aqi.module.start;

import android.content.Intent;
import android.os.Bundle;

import com.jayfeng.lesscode.core.ActivityLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;
import com.semc.aqi.model.IpInfo;
import com.semc.aqi.module.main.MainActivity;
import com.semc.aqi.repository.json.Result;

public class StartActivity extends BaseActivity<StartContract.Presenter> implements StartContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLess.$fullScreen(this);
        setContentView(R.layout.activity_start);

        setPresenter(new StartPresenter(this));

        presenter.getIpInfo("103.6.11.33");
    }


    @Override
    public void setPresenter(StartContract.Presenter preseter) {
        presenter = preseter;
    }

    @Override
    public void gotoMainActivity(Result<IpInfo> ipInfoResult) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoadError(Throwable e) {
        ToastLess.$(this, e.toString());
        finish();
    }
}
