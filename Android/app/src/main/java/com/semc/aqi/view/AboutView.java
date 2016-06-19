package com.semc.aqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AppLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

public class AboutView extends LinearLayout {

    private TextView appNameView;
    private TextView appDescView;

    public AboutView(Context context) {
        super(context);
        init(null, 0);
    }

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AboutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_about, this, true);
        appNameView = ViewLess.$(this, R.id.app_name);
        appDescView = ViewLess.$(this, R.id.app_desc);

        appNameView.setText(AppLess.$appname() + " " + AppLess.$vername());
        appDescView.setText("第一时间掌握空气质量指数！");
    }
}
