package com.semc.aqi.base;

import android.support.v4.app.Fragment;
import android.view.View;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.view.HeaderView;

public class BaseFragment<T> extends Fragment implements BaseView<T> {

    protected T presenter;

    protected HeaderView headerView;


    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    /**
     * *****************************************************************
     * optional init base views: header view, loading view
     * *****************************************************************
     */
    protected void initHeaderView(View rootView, int titleId, boolean showLeft) {
        initHeaderView(rootView, getString(titleId), showLeft);
    }

    protected void initHeaderView(View rootView, String title, boolean showLeft) {
        headerView = ViewLess.$(rootView, R.id.header);
        headerView.setTitle(title);
        headerView.showLeft(showLeft);
    }

    protected void initHeaderView(View rootView, String title) {
        headerView = ViewLess.$(rootView, R.id.header);
        headerView.setTitle(title);
    }
}
