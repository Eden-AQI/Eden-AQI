package com.semc.aqi.module.main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.LogLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.event.RankDataEvent;
import com.semc.aqi.model.Ranking;
import com.semc.aqi.model.RankingItem;
import com.semc.aqi.model.RankingItemData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class RankRealTimeFragment extends BaseFragment implements View.OnClickListener {

    public static final String KEY_TAB_INDEX = "key_tab_index";

    public static final int PARAMATER_TYPE_AQI = 0;
    public static final int PARAMATER_TYPE_SO2 = 1;
    public static final int PARAMATER_TYPE_NO2 = 2;
    public static final int PARAMATER_TYPE_PM10 = 3;
    public static final int PARAMATER_TYPE_PM25 = 4;
    public static final int PARAMATER_TYPE_CO = 5;
    public static final int PARAMATER_TYPE_O3 = 6;

    private int tabIndex = 0;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;
    private DividerItemDecoration dividerItemDecoration;

    private List<RankingItemData> listData;

    private RadioButton tabAqiButton;
    private RadioButton tabPm25Button;
    private RadioButton tabPm10Button;
    private RadioButton tabSo2Button;
    private RadioButton tabNo2Button;
    private RadioButton tabO3Button;
    private RadioButton tabCoButton;

    private HorizontalScrollView horizontalScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabIndex = getArguments().getInt(KEY_TAB_INDEX);

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rank_realtime, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

        horizontalScrollView = ViewLess.$(rootView, R.id.tabs);

        tabAqiButton = ViewLess.$(rootView, R.id.tab_aqi_btn);
        tabPm25Button = ViewLess.$(rootView, R.id.tab_pm25_btn);
        tabPm10Button = ViewLess.$(rootView, R.id.tab_pm10_btn);
        tabSo2Button = ViewLess.$(rootView, R.id.tab_so2_btn);
        tabNo2Button = ViewLess.$(rootView, R.id.tab_no2_btn);
        tabO3Button = ViewLess.$(rootView, R.id.tab_o3_btn);
        tabCoButton = ViewLess.$(rootView, R.id.tab_co_btn);

        recyclerView = ViewLess.$(rootView, R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(Color.TRANSPARENT));
        dividerItemDecoration.setHeight(DisplayLess.$dp2px(10));
        recyclerView.addItemDecoration(dividerItemDecoration);

        tabAqiButton.setOnClickListener(this);
        tabPm25Button.setOnClickListener(this);
        tabPm10Button.setOnClickListener(this);
        tabSo2Button.setOnClickListener(this);
        tabNo2Button.setOnClickListener(this);
        tabO3Button.setOnClickListener(this);
        tabCoButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        adapter = AdapterLess.$recycler(getActivity(),
                listData,
                R.layout.fragment_rank_realtime_list_item,
                new AdapterLess.RecyclerCallBack<RankingItemData>() {
                    @Override
                    public void onBindViewHolder(int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, RankingItemData rankingItemData) {
                        TextView rankCityView = recyclerViewHolder.$view(R.id.rank_city);
                        TextView rankValueView = recyclerViewHolder.$view(R.id.rank_value);

                        rankCityView.setText(rankingItemData.getSiteName());
                        rankValueView.setText(rankingItemData.getValue());
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        listData = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RankDataEvent rankDataEvent) {
        listData.clear();
        listData.addAll(RankFragment.rankings.get(tabIndex).getItems().get(PARAMATER_TYPE_AQI).getData());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == tabAqiButton) {
            updateDataByTab(PARAMATER_TYPE_AQI);
        } else if (v == tabPm25Button) {
            horizontalScrollView.scrollTo(0, 0);

            updateDataByTab(PARAMATER_TYPE_PM25);
        } else if (v == tabPm10Button) {
            horizontalScrollView.scrollTo(0, 0);

            updateDataByTab(PARAMATER_TYPE_PM10);
        } else if (v == tabSo2Button) {
            horizontalScrollView.fullScroll(View.FOCUS_RIGHT);

            updateDataByTab(PARAMATER_TYPE_SO2);
        } else if (v == tabNo2Button) {
            horizontalScrollView.fullScroll(View.FOCUS_RIGHT);

            updateDataByTab(PARAMATER_TYPE_NO2);
        } else if (v == tabO3Button) {
            horizontalScrollView.fullScroll(View.FOCUS_RIGHT);

            updateDataByTab(PARAMATER_TYPE_O3);
        } else if (v == tabCoButton) {
            updateDataByTab(PARAMATER_TYPE_CO);
        }
    }

    public void updateDataByTab(int parameter) {
        listData.clear();

        Ranking ranking = RankFragment.rankings.get(tabIndex);

        if (ranking != null) {
            List<RankingItem> rankingItems = ranking.getItems();
            if (rankingItems != null && rankingItems.size() > 0) {
                listData.addAll(rankingItems.get(parameter).getData());
            }
        }

        recyclerView.setAdapter(adapter);
    }
}
