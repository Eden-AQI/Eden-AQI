package com.semc.aqi.module.main;

import android.os.Bundle;
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
        Fragment fragment = new LatestFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(LatestFragment.KEY_CITY_ID, cityList.get(position).getId());
        fragment.setArguments(bundle);

        return fragment;
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
