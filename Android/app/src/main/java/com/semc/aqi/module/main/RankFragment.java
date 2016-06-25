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
import com.semc.aqi.event.RankDataEvent;
import com.semc.aqi.model.Ranking;
import com.semc.aqi.repository.WeatherRepository;
import com.semc.aqi.repository.services.WeatherService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RankFragment extends BaseFragment {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    public static List<Ranking> rankings;

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
        viewPager.setOffscreenPageLimit(10);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentPagerAdapter = AdapterLess.$pager(getChildFragmentManager(), 4, new AdapterLess.FullFragmentPagerCallBack() {
            @Override
            public Fragment getItem(int position) {

                Fragment fragment = new RankRealTimeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(RankRealTimeFragment.KEY_TAB_INDEX, position);
                fragment.setArguments(bundle);

                return fragment;
            }

            @Override
            public String getPageTitle(int position) {
                return getResources().getStringArray(R.array.rank_titles)[position];
            }
        });
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setViewPager(viewPager);

        requestRanking();

    }

    private void requestRanking() {
        WeatherRepository.getInstance().getRankingData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Ranking>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Ranking> rankings) {
                        RankFragment.rankings =  rankings;

                        EventBus.getDefault().post(new RankDataEvent());
                    }
                });
    }
}
