package com.semc.aqi.module.start;

import android.content.Intent;
import android.os.Bundle;

import com.jayfeng.lesscode.core.ActivityLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;
import com.semc.aqi.module.main.MainActivity;

public class StartActivity extends BaseActivity<StartContract.Presenter> implements StartContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLess.$fullScreen(this);
        setContentView(R.layout.activity_start);

        setPresenter(new StartPresenter(this));

        gotoMainActivity();
    }


    @Override
    public void setPresenter(StartContract.Presenter preseter) {
        presenter = preseter;
    }

    @Override
    public void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
