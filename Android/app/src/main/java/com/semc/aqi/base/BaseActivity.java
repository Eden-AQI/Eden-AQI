package com.semc.aqi.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.view.HeaderView;

public class BaseActivity<T> extends AppCompatActivity implements BaseView<T> {

    protected T presenter;

    protected HeaderView headerView;

    @Override
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    protected void fillContentFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
    }

    protected void initHeaderView(int titleId, boolean showLeft) {
        initHeaderView(getString(titleId), showLeft);
    }

    protected void initHeaderView(String title, boolean showLeft) {
        headerView = ViewLess.$(this, R.id.header);
        headerView.setTitle(title);
        headerView.showLeft(showLeft);
    }

    protected void initHeaderView(String title) {
        headerView = ViewLess.$(this, R.id.header);
        headerView.setTitle(title);
    }
}
