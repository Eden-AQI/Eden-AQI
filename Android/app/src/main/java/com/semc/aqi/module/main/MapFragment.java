package com.semc.aqi.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.FileLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.config.BizUtils;
import com.semc.aqi.config.Constant;
import com.semc.aqi.config.Global;
import com.semc.aqi.event.UpdateDbCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.semc.aqi.model.CityGroup;
import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.repository.WeatherRepository;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapFragment extends BaseFragment {

    private MapView mapView;
    private BaiduMap baiduMap;
    private List<CityGroup> originList;
    private List<City> showList;
    private BitmapDescriptor icon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showList = new ArrayList<>();
        icon = BitmapDescriptorFactory.fromResource(R.drawable.main_tab_map_icon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        initHeader(rootView);

        mapView = ViewLess.$(rootView, R.id.map);
        baiduMap = mapView.getMap();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (MainActivity.cityGroupList != null) {
            originList = MainActivity.cityGroupList;
            addInfosOverlay();
            centerMap();
        } else {
            updateCityDataFromServer();
        }
    }

    private void initHeader(View rootView) {
        initHeaderView(rootView, R.string.main_tab_map_text, false);
        headerView.alphaShadowDivider(0);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
    }

    private void addInfosOverlay() {
        showList.clear();

        LatLng latLng = null;

        for (int i = 0; i < originList.size(); i++) {
            CityGroup cityGroup = originList.get(i);
            List<City> groupCities = cityGroup.getItems();
            if (groupCities == null || groupCities.size() == 0) {
                continue;
            }

            for (City city : groupCities) {
                showList.add(city);
            }
        }

        for (City city : showList) {
            if (city.getAqi() > 0) {
                // 位置
                latLng = new LatLng(city.getLatitude(), city.getLongitude());
                // 文字
                OverlayOptions textOption = new TextOptions()
                        .bgColor(BizUtils.getGradleColor(city.getAqi()))
                        .fontSize(DisplayLess.$dp2px(16))
                        .fontColor(0xFFFF00FF)
                        .text(city.getAqi() + "")
                        .position(latLng);
                baiduMap.addOverlay(textOption);
            }
        }
    }

    private void centerMap() {
        LatLng center = new LatLng(Constant.ZZ_LAT, Constant.ZZ_LNG);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(center)
                .zoom(12)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void updateCityDataFromServer() {
        WeatherRepository.getInstance().getCityList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CityGroupList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastLess.$(getContext(), e.getMessage());
                    }

                    @Override
                    public void onNext(CityGroupList cityGroups) {

                        MainActivity.cityGroupList = cityGroups;

                        originList = MainActivity.cityGroupList;
                        addInfosOverlay();
                        centerMap();

                        for(CityGroup cityGroup : cityGroups) {
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
