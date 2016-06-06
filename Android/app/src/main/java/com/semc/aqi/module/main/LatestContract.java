package com.semc.aqi.module.main;

import com.semc.aqi.base.BasePresenter;
import com.semc.aqi.base.BaseView;
import com.semc.aqi.model.ForecastItem;
import com.semc.aqi.model.RealTime;

import java.util.List;

public class LatestContract {

    public static final int HOUR_TYPE_PM2P5 = 0;
    public static final int HOUR_TYPE_PM10 = 1;
    public static final int HOUR_TYPE_NO2 = 2;
    public static final int HOUR_TYPE_O3 = 3;
    public static final int HOUR_TYPE_SO2 = 4;
    public static final int HOUR_TYPE_CO = 5;

    interface Presenter extends BasePresenter {
        void requestData();
        void show24HourData(int hourType, boolean isAqi);
        void showDaysData();
    }

    interface View extends BaseView<Presenter> {
        void refreshComplete();
        void refreshError(Throwable e);
        void showAqiBasicAndDetails(RealTime realTime);
        void showHoursAqiChart(String[] labels, float[][] values, int min, int max);
        void showDaysAqiChart(String[] labels, float[][] values);
        void showAqiTable(List<ForecastItem> forecastItems);
    }
}
