package com.semc.aqi.module.image;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;

public class AqiRefActivity extends BaseActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqi_ref);

        imageView = ViewLess.$(this, R.id.image);
        imageView.setImageResource(R.drawable.aqi_ref);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
