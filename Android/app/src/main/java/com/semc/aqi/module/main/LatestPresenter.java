package com.semc.aqi.module.main;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.semc.aqi.config.Global;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.event.UpdateDbCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.general.RxBus;
import com.semc.aqi.model.City;
import com.semc.aqi.model.DaysItem;
import com.semc.aqi.model.HourDataItem;
import com.semc.aqi.model.HourItem;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.repository.WeatherRepository;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LatestPresenter implements LatestContract.Presenter {

    private LatestContract.View view;
    private RealTime realTime;

    public LatestPresenter(LatestContract.View view) {
        this.view = view;
    }

    @Override
    public void requestData(final int cityId) {

        WeatherRepository.getInstance().getRealTime(cityId + "", true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RealTime>() {
                    @Override
                    public void onCompleted() {
                        view.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.refreshComplete();
                        view.refreshError(e);
                    }

                    @Override
                    public void onNext(RealTime realTime) {

                        LatestPresenter.this.realTime = realTime;

                        // update city data in db
                        updateCityInDB(cityId);

                        // show basic
                        view.showAqiBasicAndDetails(realTime);

                        // show forecast table
                        view.showAqiTable(realTime.getForecast());

                        // show 24 hour
                        show24HourData(LatestContract.HOUR_TYPE_PM2P5, true);

                        // show days
                        showDaysData();
                    }
                });
    }

    @Override
    public void show24HourData(int hourType, boolean isAqi) {

        List<HourItem> hourItems = realTime.getHours();

        // pm2p5
        List<HourDataItem> hourDataItemList = hourItems.get(hourType).getData();
        if (hourDataItemList.size() == 0) {
            view.showHoursAqiChart(new String[]{}, new float[][]{}, 0, 0);
            return;
        }

        String[] x = new String[hourDataItemList.size()];
        float[] y = new float[hourDataItemList.size()];
        float min;
        float max;
        if (isAqi) {
            min = max = hourDataItemList.get(0).getAqi();
        } else {
            min = max = hourDataItemList.get(0).getValue();
        }

        HourDataItem hourDataItem;
        for (int i = 0; i < hourDataItemList.size(); i++) {
            hourDataItem = hourDataItemList.get(i);
            x[i] = getHourByDate(hourDataItem.getTime());
            if (isAqi) {
                y[i] = (float) hourDataItem.getAqi();
            } else {
                y[i] = (float) hourDataItem.getValue();
            }

            if (y[i] < min) {
                min = y[i];
            }
            if (y[i] > max) {
                max = y[i];
            }
        }

        float[][] yWrapper = new float[][]{y, {}};

        view.showHoursAqiChart(x, yWrapper, (int) min, (int) max);
    }

    @Override
    public void showDaysData() {
        List<DaysItem> daysItems = realTime.getDays();

        String[] x = new String[daysItems.size()];
        float[] y = new float[daysItems.size()];
        float[] y1 = new float[daysItems.size()];

        DaysItem daysItem;
        for (int i = 0; i < daysItems.size(); i++) {
            daysItem = daysItems.get(i);
            x[i] = getDayByDate(daysItem.getDay());
            y[i] = daysItem.getAqi() < 0 ? 0 : daysItem.getAqi();
            y1[i] = 0;
        }

        float[][] yWrapper = new float[][]{y, y1};

        view.showDaysAqiChart(x, yWrapper);
    }

    private String getDayByDate(String date) {
        String[] dateSpitArray = date.split("-");
        return dateSpitArray[dateSpitArray.length - 1];
    }

    private String getHourByDate(String date) {
        String result = date.split(" ")[1];

        return result;
    }

    private void updateCityInDB(int cityId) {
        LiteOrm liteOrm = LiteOrmManager.getLiteOrm(Global.getContext());
        QueryBuilder<City> queryBuilder = new QueryBuilder<>(City.class)
                .whereEquals("city_id", cityId);
        List<City> result = liteOrm.query(queryBuilder);
        if (result != null && result.size() > 0) {
            City firstCity = result.get(0);
            firstCity.setAqi(realTime.getAqi());
            liteOrm.save(firstCity);

            EventBus.getDefault().post(new UpdateDbCityEvent());
        }
    }
}
