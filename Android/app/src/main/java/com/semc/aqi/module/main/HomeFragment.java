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
import com.semc.aqi.event.CurrentCityEventFromLeft;
import com.semc.aqi.event.CurrentCityEventFromMain;
import com.semc.aqi.event.DeleteCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CurrentCityEventFromMain currentCityEventFromMain = new CurrentCityEventFromMain(position);
                EventBus.getDefault().post(currentCityEventFromMain);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                ((MainActivity) getActivity()).toggle();
            }
        });
        headerView.setLeftImage(R.drawable.header_menu_slide);
        headerView.showRightImageView(R.drawable.latest_menu_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(getContext());
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

                // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(getString(R.string.ssdk_oks_share));
                // text是分享文本，所有平台都需要这个字段

                City currentCity = cityList.get(viewPager.getCurrentItem());

                oks.setText(getString(R.string.app_name) + "\n" + currentCity.getName() + " AQI:" + currentCity.getAqi());
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                oks.setImageUrl("http://ww3.sinaimg.cn/mw690/006tFG1fjw1f591e24b8yj3040040q2x.jpg");

                // url仅在微信（包括好友和朋友圈）中使用
                int stationCode = currentCity.getId();
                oks.setUrl("http://aqi.wuhooooo.com/api/Share?stationCode=" + stationCode);


                // 启动分享GUI
                oks.show(getContext());
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

    @Subscribe
    public void onEvent(CurrentCityEventFromLeft currentCityEventFromLeft) {
        int index = currentCityEventFromLeft.getIndex();
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
