package com.semc.aqi.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RankFragment extends BaseFragment {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);

        tabLayout = ViewLess.$(rootView, R.id.tabs);
        viewPager = ViewLess.$(rootView, R.id.viewpager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentPagerAdapter = AdapterLess.$pager(getChildFragmentManager(), 4, new AdapterLess.FullFragmentPagerCallBack() {
            @Override
            public Fragment getItem(int position) {
                return new RankRealTimeFragment();
            }

            @Override
            public String getPageTitle(int position) {
                return getResources().getStringArray(R.array.rank_titles)[position];
            }
        });
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setViewPager(viewPager);
    }
}
