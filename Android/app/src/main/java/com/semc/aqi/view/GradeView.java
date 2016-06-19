package com.semc.aqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AppLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

public class GradeView extends LinearLayout {

    public GradeView(Context context) {
        super(context);
        init(null, 0);
    }

    public GradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_grade, this, true);
    }
}
