package com.semc.aqi.module.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jayfeng.lesscode.core.LogLess;
import com.jayfeng.lesscode.core.ResourceLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.config.BizUtils;
import com.semc.aqi.general.FastBlur;
import com.semc.aqi.model.ForecastItem;
import com.semc.aqi.model.OtherParameterItem;
import com.semc.aqi.model.RealTime;
import com.semc.aqi.view.AqiDetailsItemView;
import com.semc.aqi.view.GradeView;
import com.semc.aqi.view.ListViewPullHeader;
import com.semc.aqi.view.PositionScrollView;
import com.semc.aqi.view.dialog.CommonDialog;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class LatestFragment extends BaseFragment<LatestContract.Presenter> implements LatestContract.View {

    public static final String KEY_CITY_ID = "key_city_id";

    private int cityId = 0;

    private PtrClassicFrameLayout ptrFrame;
    protected ListViewPullHeader listViewPullHeader;
    private PositionScrollView scrollView;
    private RelativeLayout summaryContainer;
    private LinearLayout aqiTableContainer;

    private ImageView bgView;
    private ImageView bgOverlayView;

    private TextView aqiBasicAqiView;
    private ImageView aqiBasicLevelIconView;
    private ImageView aqiBasicKtView;
    private TextView aqiBasicLevelTextView;
    private ImageView aqiBasicLevelDetailsView;
    private TextView aqiBasicPollutantView;
    private TextView aqiBasicConcentrationView;
    private TextView aqiBasicUpdateTimeView;

    private TextView aqiDetailBigPollutantView;
    private TextView aqiDetailBigAqiView;

    private LinearLayout aqiDetailsItemContainer;
    private TextView aqiDetailsHeathDescView;
    private TextView aqiDetailsSuggestDescView;

    private SegmentTabLayout concentrationAqiTabLayout;
    private SegmentTabLayout concentrationHourTypeTabLayout;
    private LineChartView concentrationChart;
    private YLineChartView concentrationYChart;
    private StackBarChartView daysAqiChart;

    private int hourType = LatestContract.HOUR_TYPE_PM2P5;
    private boolean isAqi = true;

    private BitmapDrawable bitmapDrawable;
    private Bitmap bitmapBlur;

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

        cityId = getArguments().getInt(KEY_CITY_ID, 0);

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

        bgView = ViewLess.$(rootView, R.id.bg);
        bgOverlayView = ViewLess.$(rootView, R.id.bg_overlay);

        // 基本信息
        aqiBasicAqiView = ViewLess.$(rootView, R.id.aqi_basic_aqi);
        aqiBasicLevelIconView = ViewLess.$(rootView, R.id.aqi_basic_level_icon);
        aqiBasicKtView = ViewLess.$(rootView, R.id.aqi_basic_kt);
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
        // 健康和建议
        aqiDetailsHeathDescView = ViewLess.$(rootView, R.id.aqi_details_health_desc);
        aqiDetailsSuggestDescView = ViewLess.$(rootView, R.id.aqi_details_suggest_desc);

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
                presenter.requestData(cityId);
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

        bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.main_bg);
        bgView.setImageDrawable(bitmapDrawable);
        bgOverlayView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bitmapBlur == null) {

                    Bitmap bkg = bitmapDrawable.getBitmap();
                    bitmapBlur = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(bitmapBlur);
                    canvas.drawBitmap(bkg, 0, 0, new Paint());

                    bitmapBlur = FastBlur.doBlur(bitmapBlur, 20, true);
                    bgOverlayView.setImageBitmap(bitmapBlur);
                    bgOverlayView.setAlpha(0f);
                }
            }
        }, 400);

        autoSummaryContainerHeight();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aqiBasicLevelDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog gradeDialog = new CommonDialog(getContext());
                gradeDialog.setTitle("空气质量等级");
                gradeDialog.setContentMode(CommonDialog.CONTENT_MODE_CUSTOM);
                gradeDialog.setCustomView(new GradeView(getContext()));
                gradeDialog.hideBottom();
                gradeDialog.show();
            }
        });
        concentrationAqiTabLayout.setTabData(new String[]{"　浓度　", "实时指数"});
        concentrationHourTypeTabLayout.setTabData(new String[]{"PM2.5", "PM10", "NO2", "O3", "SO2", "CO"});

        concentrationAqiTabLayout.setOnTabSelectListener(mIsAqiTabSelectListener);
        concentrationHourTypeTabLayout.setOnTabSelectListener(mHourTypeTabSelectListener);

        scrollView.setScrollViewListener(new PositionScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(PositionScrollView scrollView, int x, int y, int oldx, int oldy) {

                LogLess.$d("=================y:" + oldy);

                if (y % 2 == 0) {
                    bgOverlayView.setAlpha((float) y / 200);
                }
            }
        });
    }

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

        aqiTableContainer.removeAllViews();
        for (int i = 0; i < forecastItems.size(); i++) {
            ForecastItem forecastItem = forecastItems.get(i);
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity())
                    .inflate(R.layout.fragment_latest_api_table_item, aqiTableContainer, false);
            linearLayout.getLayoutParams().width = width;
            TextView dateView = ViewLess.$(linearLayout, R.id.aqi_table_date);
            TextView levelView = ViewLess.$(linearLayout, R.id.aqi_table_level);
            TextView valueView = ViewLess.$(linearLayout, R.id.aqi_table_value);
            TextView primaryView = ViewLess.$(linearLayout, R.id.aqi_table_value_primary);

            dateView.setText(forecastItem.getTime());
            levelView.setText(forecastItem.getAqiLevel());
            levelView.setBackgroundResource(ResourceLess.$id(getContext(), "grade_level_bg_" + BizUtils.getGradleLevelByState(forecastItem.getAqiLevel()), ResourceLess.TYPE.DRAWABLE));
            valueView.setText(forecastItem.getAqi() + "");
            primaryView.setText(forecastItem.getPrimaryParameter());

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

        aqiDetailsHeathDescView.setText(realTime.getHealth());
        aqiDetailsSuggestDescView.setText(realTime.getSuggest());
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
                .setXAxis(true);
//                .setYLabels(AxisController.LabelPosition.NONE)
//                .setYAxis(false);
        concentrationYChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisColor(Color.parseColor("#ffffff"))
                .setAxisBorderValues(min, max)
                .setStep((max - min) / 5)
                .setAxisThickness(2)
                .setLabelsColor(Color.parseColor("#ffffff"))
//                .setXLabels(AxisController.LabelPosition.NONE)
//                .setXAxis(false)
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

    private void showBasicLevelIcon(int aqi) {
        int level = BizUtils.getGradleLevel(aqi);
        int levelIcon = ResourceLess.$id(getActivity(), "aqi_level_icon_" + level, ResourceLess.TYPE.DRAWABLE);
        int ktIcon = ResourceLess.$id(getActivity(), "aqi_kt_level_" + level, ResourceLess.TYPE.DRAWABLE);
        aqiBasicLevelIconView.setImageResource(levelIcon);
        aqiBasicKtView.setImageResource(ktIcon);
        aqiBasicLevelTextView.setText(BizUtils.getGradleText(aqi));
    }
}