package com.semc.aqi.module.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jayfeng.lesscode.core.DrawableLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;

public class MainActivity extends BaseActivity implements RadioButton.OnCheckedChangeListener {

    private static final String TAG_HOME = "home";
    private static final String TAG_MAP = "map";
    private static final String TAG_RANK = "rank";
    private static final String TAG_SETTING = "setting";

    protected FragmentManager fragmentManager;
    protected Fragment currentFragment;
    protected Fragment homeFragment;
    protected Fragment mapFragment;
    protected Fragment rankFragment;
    protected Fragment settingFragment;

    private RadioButton homeTabBtn;
    private RadioButton mapTabBtn;
    private RadioButton rankTabBtn;
    private RadioButton settingTabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initFragment(savedInstanceState);

        initListener();
    }

    private void init() {

        fragmentManager = getSupportFragmentManager();

        homeTabBtn = ViewLess.$(this, R.id.tab_home_btn);
        mapTabBtn = ViewLess.$(this, R.id.tab_map_btn);
        rankTabBtn = ViewLess.$(this, R.id.tab_rank_btn);
        settingTabBtn = ViewLess.$(this, R.id.tab_setting_btn);

        homeTabBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                DrawableLess.$tint(getResources().getDrawable(R.drawable.main_tab_latest_icon), getResources().getColorStateList(R.color.global_item_drawable_tint_color_white)),
                null,
                null);

        mapTabBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                DrawableLess.$tint(getResources().getDrawable(R.drawable.main_tab_map_icon), getResources().getColorStateList(R.color.global_item_drawable_tint_color_white)),
                null,
                null);

        rankTabBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                DrawableLess.$tint(getResources().getDrawable(R.drawable.main_tab_rank_icon), getResources().getColorStateList(R.color.global_item_drawable_tint_color_white)),
                null,
                null);

        settingTabBtn.setCompoundDrawablesWithIntrinsicBounds(null,
                DrawableLess.$tint(getResources().getDrawable(R.drawable.main_tab_setting_icon), getResources().getColorStateList(R.color.global_item_drawable_tint_color_white)),
                null,
                null);
    }

    private void initListener() {
        homeTabBtn.setOnCheckedChangeListener(this);
        mapTabBtn.setOnCheckedChangeListener(this);
        rankTabBtn.setOnCheckedChangeListener(this);
        settingTabBtn.setOnCheckedChangeListener(this);
    }

    protected void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentFragment = homeFragment = fragmentManager.findFragmentByTag(TAG_HOME);
            mapFragment = fragmentManager.findFragmentByTag(TAG_MAP);
            rankFragment = fragmentManager.findFragmentByTag(TAG_RANK);
            settingFragment = fragmentManager.findFragmentByTag(TAG_SETTING);

            fragmentManager.beginTransaction().show(currentFragment)
                    .hide(mapFragment)
                    .hide(rankFragment)
                    .hide(settingFragment)
                    .commit();
        } else {
            currentFragment = homeFragment = new HomeFragment();
            mapFragment = new MapFragment();
            rankFragment = new RankFragment();
            settingFragment = new SettingFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragments, homeFragment, TAG_HOME).commit();
        }
    }

    public void changeFrament(Fragment fragment, String fragmentTag) {

        if (fragment == currentFragment) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.hide(currentFragment).add(R.id.fragments, fragment, fragmentTag).commit();
        } else {
            fragmentTransaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int buttonViewId = buttonView.getId();
            if (buttonViewId == R.id.tab_home_btn) {
                changeFrament(homeFragment, TAG_HOME);

            } else if (buttonViewId == R.id.tab_map_btn) {
                changeFrament(mapFragment, TAG_MAP);

            } else if (buttonViewId == R.id.tab_rank_btn) {
                changeFrament(rankFragment, TAG_RANK);

            } else if (buttonViewId == R.id.tab_setting_btn) {
                changeFrament(settingFragment, TAG_SETTING);

            }
        }
    }

}
