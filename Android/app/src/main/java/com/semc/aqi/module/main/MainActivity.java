package com.semc.aqi.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.DrawableLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.semc.aqi.R;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.event.DeleteCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.semc.aqi.module.city.AddCityActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends SlidingFragmentActivity implements RadioButton.OnCheckedChangeListener {

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

    private LocationClient mLocationClient;
    private BDLocationListener mLocationListener = new CustomLocationListener();
    private boolean mIsLocated = false;
    private int mLocationRetry = 0;
    private final static int LOCATION_MAX_RETRY = 8;
    private Handler mStopBaiduLocationHandler;

    // slide menu
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;
    private DividerItemDecoration dividerItemDecoration;
    private ImageButton addCityView;
    private List<City> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slide_menu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        setBehindContentView(R.layout.activity_main_left);

        EventBus.getDefault().register(this);

        init();
        initSlideMenu();
        initFragment(savedInstanceState);

        initListener();

        // 百度定位
        mStopBaiduLocationHandler = new Handler();
        initBaiduLocation();
        confirmStopBaiduLocation();

//        JPushInterface.getRegistrationID(this);
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

    private void initSlideMenu() {
        addCityView = ViewLess.$(this, R.id.add);
        recyclerView = ViewLess.$(this, R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(Color.parseColor("#33ffffff")));
        dividerItemDecoration.setHeight(DisplayLess.$dp2px(1));
        recyclerView.addItemDecoration(dividerItemDecoration);

        list = LiteOrmManager.getLiteOrm(this).query(City.class);

        adapter = AdapterLess.$recycler(this,
                list,
                R.layout.activity_main_left_list_item,
                new AdapterLess.RecyclerCallBack<City>() {
                    @Override
                    public void onBindViewHolder(final int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, final City city) {
                        View container = recyclerViewHolder.$view(R.id.container);
                        TextView nameView = recyclerViewHolder.$view(R.id.name);
                        nameView.setText(city.getName());

                        container.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("你确定要删除这个城市吗？");
                                builder.setTitle("提示");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        WhereBuilder whereBuilder = new WhereBuilder(City.class, "code = ?", new String[]{list.get(position).getId() + ""});
                                        LiteOrmManager.getLiteOrm(MainActivity.this).delete(whereBuilder);
                                        list.remove(position);

                                        adapter.notifyDataSetChanged();

                                        EventBus.getDefault().post(new DeleteCityEvent());
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                                return true;
                            }
                        });
                    }
                });

        recyclerView.setAdapter(adapter);
        addCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStopBaiduLocationHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    /**
     * ***************************************************************
     * 百度定位
     * ***************************************************************
     */
    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);
        final LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");// gcj02, bd09ll, bd09
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLocationClient.setLocOption(option);
                mLocationClient.start();
            }
        }).start();

    }

    public class CustomLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            if (mIsLocated) {
                return;
            }
            if (mLocationRetry >= LOCATION_MAX_RETRY) {
                mLocationClient.stop();
                return;
            }

            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());

            sb.append("\ncity : ");
            sb.append(location.getCity());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());

            if (location.getLocType() == 161
                    || location.getLocType() == 66) {
                mLocationClient.stop();
                mIsLocated = true;

                ToastLess.$(MainActivity.this, location.getCity());

            } else {
                mLocationRetry++;
            }

        }
    }

    private void confirmStopBaiduLocation() {
        mStopBaiduLocationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.stop();
                }
            }
        }, 10000);
    }

    @Subscribe
    public void onEvent(AddCityEvent addCityEvent) {
        list.clear();
        list.addAll(LiteOrmManager.getLiteOrm(this).query(City.class));
        adapter.notifyDataSetChanged();
    }
}
