package com.semc.aqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AppLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

public class UpdateView extends LinearLayout {

    private TextView logView;
    private Button confirmButton;
    private Button cancelButton;

    public UpdateView(Context context) {
        super(context);
        init(null, 0);
    }

    public UpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public UpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_update, this, true);
        logView = ViewLess.$(this, R.id.log);
        confirmButton = ViewLess.$(this, R.id.confirm);
        cancelButton = ViewLess.$(this, R.id.cancel);
    }

    public void setLog(String text) {
        logView.setText(text);
    }

    public void setConfirmOnClickListener(View.OnClickListener confirmOnClickListener) {
        if (confirmButton != null && confirmOnClickListener != null) {
            confirmButton.setOnClickListener(confirmOnClickListener);
        }
    }

    public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
        if (cancelButton != null && cancelOnClickListener != null) {
            cancelButton.setOnClickListener(cancelOnClickListener);
        }
    }
}
