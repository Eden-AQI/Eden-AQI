package com.semc.aqi.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.jayfeng.lesscode.core.DeviceLess;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.DrawableLess;
import com.jayfeng.lesscode.core.ResourceLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.UpdateLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.semc.aqi.R;
import com.semc.aqi.config.BizUtils;
import com.semc.aqi.config.Global;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.event.CurrentCityEventFromLeft;
import com.semc.aqi.event.CurrentCityEventFromMain;
import com.semc.aqi.event.DeleteCityEvent;
import com.semc.aqi.event.UpdateDbCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.semc.aqi.model.CityGroup;
import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.model.Device;
import com.semc.aqi.model.Update;
import com.semc.aqi.module.city.AddCityActivity;
import com.semc.aqi.repository.WeatherRepository;
import com.semc.aqi.view.UpdateView;
import com.semc.aqi.view.dialog.CommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends SlidingFragmentActivity implements RadioButton.OnCheckedChangeListener {

    private static final String TAG_HOME = "home";
    private static final String TAG_MAP = "map";
    private static final String TAG_RANK = "rank";
    private static final String TAG_SETTING = "setting";

    public static CityGroupList cityGroupList;

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

    private int selectedIndex = 0;

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

        heartbeat();
        updateCityDataFromServer();
        checkUpdate();
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
                        final View container = recyclerViewHolder.$view(R.id.container);
                        TextView nameView = recyclerViewHolder.$view(R.id.name);
                        TextView aqiView = recyclerViewHolder.$view(R.id.aqi);
                        nameView.setText(city.getName());
                        aqiView.setText(city.getAqi() + "");

                        int level = BizUtils.getGradleLevel(city.getAqi());
                        aqiView.setBackgroundResource(ResourceLess.$id(MainActivity.this, "grade_level_bg_" + level, ResourceLess.TYPE.DRAWABLE));

                        if (selectedIndex == position) {
                            container.setSelected(true);
                        } else {
                            container.setSelected(false);
                        }

                        container.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toggle();
                                EventBus.getDefault().post(new CurrentCityEventFromLeft(position));
                            }
                        });

                        container.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                if (position == 0) {
                                    return false;
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("你确定要删除这个城市吗？");
                                builder.setTitle("提示");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        WhereBuilder whereBuilder = new WhereBuilder(City.class, "city_id = ?", new String[]{list.get(position).getId() + ""});
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

                if (TextUtils.isEmpty(Global.getDeviceNumber())) {
                    registerDevice(location.getLatitude(), location.getLongitude());
                }
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


    /**
     * 注册设备信息
     *
     * @param latitude
     * @param longitude
     */
    private void registerDevice(double latitude, double longitude) {

        String deviceNumber = DeviceLess.$mac();
        if (!TextUtils.isEmpty(deviceNumber)) {
            deviceNumber = deviceNumber.trim();
        }

        Device device = new Device();
        device.setDeviceNumber(deviceNumber);
        device.setDeviceType(DisplayLess.$tablet(this) ? Device.DEVICE_TYPE_TABLET : Device.DEVICE_TYPE_PHONE);
        device.setPushId(JPushInterface.getRegistrationID(this));
        device.setLatitude(latitude);
        device.setLongitude(longitude);

        WeatherRepository.getInstance().registerDevice(device)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                        String deviceNumber = DeviceLess.$mac();
                        if (!TextUtils.isEmpty(deviceNumber)) {
                            deviceNumber = deviceNumber.trim();
                        }

                        Global.setDeviceNumber(deviceNumber);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastLess.$(MainActivity.this, e.toString());

                        String deviceNumber = DeviceLess.$mac();
                        if (!TextUtils.isEmpty(deviceNumber)) {
                            deviceNumber = deviceNumber.trim();
                        }

                        Global.setDeviceNumber(deviceNumber);
                    }

                    @Override
                    public void onNext(String string) {
//                        ToastLess.$(MainActivity.this, "注册成功");
                    }
                });
    }

    private void heartbeat() {

        String deviceNumber = Global.getDeviceNumber();
        if (TextUtils.isEmpty(deviceNumber)) {
            return;
        }

        WeatherRepository.getInstance().heartbeat(deviceNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastLess.$(MainActivity.this, e.toString());
                    }

                    @Override
                    public void onNext(String string) {
//                        ToastLess.$(MainActivity.this, "心跳成功");
                    }
                });
    }

    private void updateCityList() {
        list.clear();
        list.addAll(LiteOrmManager.getLiteOrm(this).query(City.class));
        adapter.notifyDataSetChanged();
    }

    private void updateCityDataFromServer() {
        WeatherRepository.getInstance().getCityList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<CityGroupList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CityGroupList cityGroups) {

                        cityGroupList = cityGroups;

                        for (CityGroup cityGroup : cityGroups) {
                            List<City> cities = cityGroup.getItems();
                            for (City city : cities) {
                                LiteOrm liteOrm = LiteOrmManager.getLiteOrm(Global.getContext());
                                QueryBuilder<City> queryBuilder = new QueryBuilder<>(City.class)
                                        .whereEquals("city_id", city.getId());
                                List<City> result = liteOrm.query(queryBuilder);
                                if (result != null && result.size() > 0) {
                                    City firstCity = result.get(0);
                                    firstCity.setAqi(city.getAqi());
                                    liteOrm.save(firstCity);
                                }
                            }
                        }

                        EventBus.getDefault().post(new UpdateDbCityEvent());
                    }
                });
    }

    private void checkUpdate() {

        WeatherRepository.getInstance().checkUpdate()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Update>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastLess.$(MainActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(final Update update) {

                        boolean hasUpdate = UpdateLess.$hasUpdate(update.getVersionCode());


                        if (hasUpdate) {

                            final CommonDialog updateDialog = new CommonDialog(MainActivity.this);
                            updateDialog.setTitle("发现新版本：" + update.getVersionName());
                            updateDialog.setContentMode(CommonDialog.CONTENT_MODE_CUSTOM);

                            final UpdateView updateView = new UpdateView(MainActivity.this);
                            String log = update.getDescription();
                            if (TextUtils.isEmpty(log)) {
                                log = "修复大量bug";
                            }
                            updateView.setLog(log);
                            updateView.setConfirmOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateLess.$download(MainActivity.this, update.getDownloadUrl());
                                    if (update.isMandatory()) {
                                        updateView.setConfirmText("正在更新...");
                                    } else {
                                        updateDialog.dismiss();
                                    }
                                }
                            });
                            updateView.setCancelOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateDialog.dismiss();
                                }
                            });
                            if (update.isMandatory()) {
                                updateView.hideCancelButton();
                            }

                            if (update.isMandatory()) {
                                updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent keyEvent) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                            MainActivity.this.onBackPressed();
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                            }

                            updateDialog.setCustomView(updateView);
                            updateDialog.hideBottom();
                            updateDialog.show();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddCityEvent addCityEvent) {
        updateCityList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateDbCityEvent updateDbCityEvent) {
        updateCityList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CurrentCityEventFromMain currentCityEventFromMain) {
        selectedIndex = currentCityEventFromMain.getIndex();
        adapter.notifyDataSetChanged();
    }
}
