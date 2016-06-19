package com.semc.aqi.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayfeng.lesscode.core.DisplayLess;
import com.semc.aqi.R;

public class LoadingDialog extends Dialog {

    private TextView mLoadingText;
    private ImageView mProgressView;
    private Animation mProgressAnim;

    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogTheme);
        init();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.view_dialog_loading);

        mLoadingText = (TextView) findViewById(R.id.text);
        mProgressView = (ImageView) findViewById(R.id.progress);

        mProgressAnim = AnimationUtils.loadAnimation(getContext(), R.anim.view_dialog_loading_progressbar);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mProgressAnim.setInterpolator(linearInterpolator);
        mProgressAnim.setFillAfter(true);
    }

    public void setLoadingText(String text) {
        mLoadingText.setText(text);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = DisplayLess.$dp2px(160);
        lp.height = DisplayLess.$dp2px(100);
        getWindow().setAttributes(lp);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mProgressAnim != null) {
                    mProgressView.startAnimation(mProgressAnim);
                }
            }
        });
    }

    @Override
    public void cancel() {
        super.cancel();

        mProgressView.clearAnimation();
    }
}
