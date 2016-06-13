package com.semc.aqi.module.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.MapView;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;

public class MapFragment extends BaseFragment {

    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        initHeader(rootView);;

        mapView = ViewLess.$(rootView, R.id.map);

        return rootView;
    }

    private void initHeader(View rootView) {
        initHeaderView(rootView, R.string.main_tab_map_text, false);
        headerView.alphaShadowDivider(0);
        headerView.setBgColor(android.R.color.transparent);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
    }

    @Override
    public void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }
}
