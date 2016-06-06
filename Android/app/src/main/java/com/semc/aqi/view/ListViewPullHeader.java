package com.semc.aqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class ListViewPullHeader extends RelativeLayout implements PtrUIHandler {

    private TextView textView;

    public ListViewPullHeader(Context context) {
        super(context);
        initView(context);
    }

    public ListViewPullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ListViewPullHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View header = LayoutInflater.from(context).inflate(R.layout.view_ptr_header, this);
        textView = ViewLess.$(header, R.id.text);
    }

    @Override
    public void onUIReset(PtrFrameLayout ptrFrameLayout) {
        textView.setText(R.string.pull_refresh_pull);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
        textView.setText(R.string.pull_refresh_pull);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        textView.setText(R.string.pull_refresh_begin);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout ptrFrameLayout) {
        textView.setText(R.string.pull_refresh_complete);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout ptrFrameLayout, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        int mOffsetToRefresh = ptrFrameLayout.getOffsetToRefresh();
        int currentPos = ptrIndicator.getCurrentPosY();
        int lastPos = ptrIndicator.getLastPosY();
        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh && isUnderTouch && status == 2) {
            if (!ptrFrameLayout.isPullToRefresh()) {
                textView.setText(R.string.pull_refresh_release);
            }
        }
    }
}
