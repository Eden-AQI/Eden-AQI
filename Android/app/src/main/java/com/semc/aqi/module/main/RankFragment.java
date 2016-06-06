package com.semc.aqi.module.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;

public class RankFragment extends BaseFragment<RankContract.Presenter> implements RankContract.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new RankPresenter(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        initHeader(rootView);
        return rootView;
    }

    private void initHeader(View rootView) {
        initHeaderView(rootView, R.string.main_tab_rank_text, false);
        headerView.alphaShadowDivider(0);
        headerView.setBgColor(android.R.color.transparent);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
    }
}
