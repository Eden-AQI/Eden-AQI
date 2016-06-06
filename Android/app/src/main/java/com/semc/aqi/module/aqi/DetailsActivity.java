package com.semc.aqi.module.aqi;

import android.os.Bundle;

import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;

public class DetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initHeaderView(getTitle().toString(), true);
        headerView.alphaShadowDivider(0);
        headerView.setBgColor(android.R.color.transparent);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
    }
}
