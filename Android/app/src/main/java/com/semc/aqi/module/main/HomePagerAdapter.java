package com.semc.aqi.module.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semc.aqi.model.City;

import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<City> cityList;

    public HomePagerAdapter(FragmentManager fm, List<City> cityList) {
        super(fm);
        this.cityList = cityList;
    }

    @Override
    public Fragment getItem(int position) {
        return new LatestFragment();
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
