package com.semc.aqi.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.event.DeleteCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private CirclePageIndicator indicator;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private List<City> cityList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initHeader(rootView);

        indicator = ViewLess.$(rootView, R.id.indicator);
        viewPager = ViewLess.$(rootView, R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        initData(false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData(boolean addCity) {
        if (cityList == null) {
            cityList = LiteOrmManager.getLiteOrm(getActivity()).query(City.class);
        } else {
            cityList.clear();
            cityList.addAll(LiteOrmManager.getLiteOrm(getActivity()).query(City.class));
        }
        if (cityList.size() < 2) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
        }
        if (pagerAdapter == null) {
            pagerAdapter = new HomePagerAdapter(getChildFragmentManager(), cityList);
            viewPager.setAdapter(pagerAdapter);

            headerView.setTitle(cityList.get(0).getName());
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    headerView.setTitle(cityList.get(position).getName());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            pagerAdapter.notifyDataSetChanged();
        }
        indicator.setViewPager(viewPager);

        if (!addCity) {
            indicator.setCurrentItem(0);
        } else {
            indicator.setCurrentItem(cityList.size() - 1);
        }
    }

    private void initHeader(View rootView) {
        initHeaderView(rootView, R.string.main_tab_home_text, false);
        headerView.alphaShadowDivider(0);
        headerView.setBgColor(android.R.color.transparent);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));

        // menu: add and share
        headerView.showLeftBackView(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(), CityActivity.class);
                // startActivity(intent);
                ((MainActivity)getActivity()).toggle();
            }
        });
        headerView.setLeftImage(R.drawable.header_menu_slide);
        headerView.showRightImageView(R.drawable.latest_menu_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "欢迎使用上海空气质量，下载地址：http://www.baidu.com");
                shareIntent.setType("text/plain");

                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });

    }

    @Subscribe
    public void onEvent(AddCityEvent addCityEvent) {
        initData(true);
    }

    @Subscribe
    public void onEvent(DeleteCityEvent deleteCityEvent) {
        if (cityList == null) {
            cityList = LiteOrmManager.getLiteOrm(getActivity()).query(City.class);
        } else {
            cityList.clear();
            cityList.addAll(LiteOrmManager.getLiteOrm(getActivity()).query(City.class));
        }
        if (cityList.size() < 2) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
        }
        pagerAdapter.notifyDataSetChanged();
        indicator.setViewPager(viewPager);
        indicator.setCurrentItem(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
