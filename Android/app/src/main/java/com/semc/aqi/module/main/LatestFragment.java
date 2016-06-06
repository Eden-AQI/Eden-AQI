package com.semc.aqi.module.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.StackBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YLineChartView;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.model.ForecastItem;
import com.semc.aqi.model.OtherParameterItem;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.module.aqi.DetailsActivity;
import com.semc.aqi.view.AqiDetailsItemView;
import com.semc.aqi.view.ListViewPullHeader;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class LatestFragment extends BaseFragment<LatestContract.Presenter> implements LatestContract.View {

    private PtrClassicFrameLayout ptrFrame;
    protected ListViewPullHeader listViewPullHeader;
    private ScrollView scrollView;
    private RelativeLayout summaryContainer;
    private LinearLayout aqiTableContainer;

    private TextView aqiBasicAqiView;
    private ImageView aqiBasicLevelIconView;
    private TextView aqiBasicLevelTextView;
    private ImageView aqiBasicLevelDetailsView;
    private TextView aqiBasicPollutantView;
    private TextView aqiBasicConcentrationView;
    private TextView aqiBasicUpdateTimeView;

    private TextView aqiDetailBigPollutantView;
    private TextView aqiDetailBigAqiView;

    private LinearLayout aqiDetailsItemContainer;

    private SegmentTabLayout concentrationAqiTabLayout;
    private SegmentTabLayout concentrationHourTypeTabLayout;
    private LineChartView concentrationChart;
    private YLineChartView concentrationYChart;
    private StackBarChartView daysAqiChart;

    private int hourType = LatestContract.HOUR_TYPE_PM2P5;
    private boolean isAqi = true;

    private OnTabSelectListener mIsAqiTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            isAqi = (position == 0);
            presenter.show24HourData(hourType, isAqi);
        }

        @Override
        public void onTabReselect(int position) {

        }
    };

    private OnTabSelectListener mHourTypeTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            concentrationAqiTabLayout.setOnTabSelectListener(null);
            concentrationAqiTabLayout.setCurrentTab(0);
            isAqi = true;

            hourType = position;
            presenter.show24HourData(hourType, isAqi);

            concentrationAqiTabLayout.setOnTabSelectListener(mIsAqiTabSelectListener);
        }

        @Override
        public void onTabReselect(int position) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new LatestPresenter(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_latest, container, false);
        // init header
//        initHeader(rootView);

        ptrFrame = ViewLess.$(rootView, R.id.ptr_classic_frame);
        listViewPullHeader = new ListViewPullHeader(getActivity());
        scrollView = ViewLess.$(rootView, R.id.scrollview);
        summaryContainer = ViewLess.$(rootView, R.id.summary_container);
        aqiTableContainer = ViewLess.$(rootView, R.id.aqi_table);

        // 基本信息
        aqiBasicAqiView = ViewLess.$(rootView, R.id.aqi_basic_aqi);
        aqiBasicLevelIconView = ViewLess.$(rootView, R.id.aqi_basic_level_icon);
        aqiBasicLevelTextView = ViewLess.$(rootView, R.id.aqi_basic_level_text);
        aqiBasicLevelDetailsView = ViewLess.$(rootView, R.id.aqi_basic_level_details);
        aqiBasicPollutantView = ViewLess.$(rootView, R.id.aqi_basic_pollutant);
        aqiBasicConcentrationView = ViewLess.$(rootView, R.id.aqi_basic_concentration);
        aqiBasicUpdateTimeView = ViewLess.$(rootView, R.id.aqi_basic_update_time);

        // 详情
        // 大圆圈
        aqiDetailBigPollutantView = ViewLess.$(rootView, R.id.aqi_details_big_pollutant);
        aqiDetailBigAqiView = ViewLess.$(rootView, R.id.aqi_details_big_aqi);
        // 列表
        aqiDetailsItemContainer = ViewLess.$(rootView, R.id.aqi_details_items_container);

        concentrationAqiTabLayout = ViewLess.$(rootView, R.id.linechart_aqi_tab);
        concentrationHourTypeTabLayout = ViewLess.$(rootView, R.id.linechart_hour_type_tab);
        concentrationChart = ViewLess.$(rootView, R.id.linechart_concentration);
        concentrationYChart = ViewLess.$(rootView, R.id.linechart_concentration_y);
        daysAqiChart = ViewLess.$(rootView, R.id.stackbarchart_days_aqi);

        ptrFrame.setLastUpdateTimeRelateObject(this);
        ptrFrame.setPinContent(true);
        ptrFrame.setHeaderView(listViewPullHeader);
        ptrFrame.addPtrUIHandler(listViewPullHeader);
        ptrFrame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.requestData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, scrollView, header);
            }
        });


        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh();
            }
        }, 100);

        autoSummaryContainerHeight();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aqiBasicLevelDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                startActivity(intent);
            }
        });
        concentrationAqiTabLayout.setTabData(new String[]{"　浓度　", "实时指数"});
        concentrationHourTypeTabLayout.setTabData(new String[]{"PM2.5", "PM10", "NO2", "O3", "SO2", "CO"});

        concentrationAqiTabLayout.setOnTabSelectListener(mIsAqiTabSelectListener);
        concentrationHourTypeTabLayout.setOnTabSelectListener(mHourTypeTabSelectListener);
    }

//    private void initHeader(View rootView) {
//        initHeaderView(rootView, "上海空气质量", true);
//        headerView.alphaShadowDivider(0);
//        headerView.setBgColor(android.R.color.transparent);
//        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
//        // menu: add and share
//        headerView.showLeftBackView(true, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CityActivity.class);
//                startActivity(intent);
//            }
//        });
//        headerView.setLeftImage(R.drawable.latest_menu_add);
//        headerView.showRightImageView(R.drawable.latest_menu_share, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "欢迎使用上海空气质量，下载地址：http://www.baidu.com");
//                shareIntent.setType("text/plain");
//
//                //设置分享列表的标题，并且每次都显示分享列表
//                startActivity(Intent.createChooser(shareIntent, "分享到"));
//            }
//        });
//    }

    private void autoSummaryContainerHeight() {
        int screenHeight = DisplayLess.$height(getActivity());
        int statusBarHeight = DisplayLess.$statusBarHeight(getResources());
        int headerHeight = (int) getResources().getDimension(R.dimen.header_height);
        int mainTabHeight = (int) getResources().getDimension(R.dimen.main_tab_height);
        summaryContainer.getLayoutParams().height =
                screenHeight - statusBarHeight - mainTabHeight - headerHeight - DisplayLess.$dp2px(16);
    }

    @Override
    public void showAqiTable(List<ForecastItem> forecastItems) {

        int width = DisplayLess.$width(getActivity()) / 3;
        for (int i = 0; i < forecastItems.size(); i++) {
            ForecastItem forecastItem = forecastItems.get(i);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity())
                    .inflate(R.layout.fragment_latest_api_table_item, aqiTableContainer, false);
            linearLayout.getLayoutParams().width = width;
            TextView dateView = ViewLess.$(linearLayout, R.id.aqi_table_date);
            TextView levelView = ViewLess.$(linearLayout, R.id.aqi_table_level);
            TextView valueView = ViewLess.$(linearLayout, R.id.aqi_table_value);
            TextView degreeView = ViewLess.$(linearLayout, R.id.aqi_table_degree);

            dateView.setText(forecastItem.getTime());
            levelView.setText(forecastItem.getAqiLevel());
            valueView.setText(forecastItem.getAqi() + "");
            degreeView.setText(forecastItem.getTemperature());

            aqiTableContainer.addView(linearLayout);

            if (i < forecastItems.size() - 1) {
                View dividerView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.fragment_latest_api_table_divider, aqiTableContainer, false);
                aqiTableContainer.addView(dividerView);
            }
        }
    }

    @Override
    public void showAqiBasicAndDetails(RealTime realTime) {

        // 基本信息
        aqiBasicAqiView.setText(realTime.getAqi() + "");
        aqiBasicPollutantView.setText("首要污染物：" + realTime.getPrimaryParameter());
        aqiBasicConcentrationView.setText("浓度：" + realTime.getPrimaryValue());
        aqiBasicUpdateTimeView.setText(realTime.getUpdateTime());

        showBasicLevelIcon(realTime.getAqi());

        // 详情
        aqiDetailBigPollutantView.setText("首要污染物：" + realTime.getPrimaryParameter());
        aqiDetailBigAqiView.setText(realTime.getAqi() + "");

        List<OtherParameterItem> otherParameterItems = realTime.getOtherParameters();
        aqiDetailsItemContainer.removeAllViews();
        for (OtherParameterItem item : otherParameterItems) {
            AqiDetailsItemView aqiDetailsItemView = new AqiDetailsItemView(getActivity());
            aqiDetailsItemView.setTitle(item.getName());
            aqiDetailsItemView.setText(item.getValue());
            aqiDetailsItemContainer.addView(aqiDetailsItemView);
        }
    }

    @Override
    public void showHoursAqiChart(String[] labels, float[][] values, int min, int max) {

        concentrationChart.reset();
        concentrationYChart.reset();

        if (labels.length == 0) {
            ToastLess.$(getActivity(), "暂无数据");
            concentrationChart.setVisibility(View.INVISIBLE);
            concentrationYChart.setVisibility(View.INVISIBLE);
            return;
        } else {
            concentrationChart.setVisibility(View.VISIBLE);
            concentrationYChart.setVisibility(View.VISIBLE);
        }

        min = min - min % 10;
        max = max + 10 - max % 10;

        LineSet dataset = new LineSet(labels, values[0]);
        dataset.setColor(Color.parseColor("#ffffff"))
                .setDotsColor(Color.parseColor("#ffffff"))
                .setThickness(2);
        LineSet datasetY = new LineSet(labels, values[0]);
        datasetY.setColor(Color.parseColor("#ffffff"))
                .setDotsColor(Color.parseColor("#ffffff"))
                .setThickness(2);
        concentrationChart.addData(dataset);
        concentrationYChart.addData(datasetY);

        // Chart
        concentrationChart.setBorderSpacing(Tools.fromDpToPx(8))
                .setAxisColor(Color.parseColor("#ffffff"))
                .setAxisBorderValues(min, max)
                .setStep((max - min) / 5)
                .setAxisThickness(2)
                .setLabelsColor(Color.parseColor("#ffffff"))
                .setXAxis(true)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setYAxis(false);
        concentrationYChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisColor(Color.parseColor("#ffffff"))
                .setAxisBorderValues(min, max)
                .setStep((max - min) / 5)
                .setAxisThickness(2)
                .setLabelsColor(Color.parseColor("#ffffff"))
                .setXLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(true);
        concentrationChart.show();
        concentrationYChart.show();
    }

    @Override
    public void showDaysAqiChart(String[] labels, float[][] values) {

        daysAqiChart.reset();

        BarSet stackBarSet = new BarSet(labels, values[0]);
        stackBarSet.setColor(Color.parseColor("#a1d949"));
        daysAqiChart.addData(stackBarSet);

        stackBarSet = new BarSet(labels, values[1]);
        stackBarSet.setColor(Color.parseColor("#ff7a57"));
        daysAqiChart.addData(stackBarSet);

        daysAqiChart.setBarSpacing(Tools.fromDpToPx(15));
        daysAqiChart.setRoundCorners(Tools.fromDpToPx(1));

        daysAqiChart.setXAxis(true)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYAxis(false)
                .setYLabels(XController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#ffffff"))
                .setAxisColor(Color.parseColor("#ffffff"))
                .setAxisThickness(2);

        daysAqiChart.show();
    }


    @Override
    public void refreshComplete() {
        ptrFrame.refreshComplete();
        summaryContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshError(Throwable e) {
        ToastLess.$(getActivity(), e.toString());
        ptrFrame.refreshComplete();
    }

    private void showBasicLevelIcon(int level) {
        int resource = R.drawable.aqi_level_icon_6;
        if (level < 51) {
            resource = R.drawable.aqi_level_icon_1;
        } else if (level < 101) {
            resource = R.drawable.aqi_level_icon_2;
        } else if (level < 151) {
            resource = R.drawable.aqi_level_icon_3;
        } else if (level < 201) {
            resource = R.drawable.aqi_level_icon_4;
        } else if (level < 301) {
            resource = R.drawable.aqi_level_icon_5;
        }

        aqiBasicLevelIconView.setImageResource(resource);
    }
}