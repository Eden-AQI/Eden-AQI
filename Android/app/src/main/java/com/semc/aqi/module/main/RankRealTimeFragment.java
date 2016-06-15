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
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.DisplayLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.model.RankRealTimeItem;

import java.util.ArrayList;
import java.util.List;

public class RankRealTimeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;
    private DividerItemDecoration dividerItemDecoration;

    private List<RankRealTimeItem> listData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        recyclerView = ViewLess.$(rootView, R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(Color.TRANSPARENT));
        dividerItemDecoration.setHeight(DisplayLess.$dp2px(10));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        adapter = AdapterLess.$recycler(getActivity(),
                listData,
                R.layout.fragment_rank_realtime_list_item,
                new AdapterLess.RecyclerCallBack<RankRealTimeItem>() {
                    @Override
                    public void onBindViewHolder(int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, RankRealTimeItem rankRealTimeItem) {
                        TextView rankNoView = recyclerViewHolder.$view(R.id.rank_no);
                        TextView rankCityView = recyclerViewHolder.$view(R.id.rank_city);
                        TextView rankValueView = recyclerViewHolder.$view(R.id.rank_value);

                        rankNoView.setText(position + "");
                        rankCityView.setText(rankRealTimeItem.getCity());
                        rankValueView.setText(rankRealTimeItem.getValue());
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        listData = new ArrayList<>();

        String[] citys = new String[] {"青岛市", "上海市", "呼和浩特市", "深圳市", "太原区", "海口市", "北京市", "杭州市", "长沙市", "深圳"};

        for (int i = 0; i < citys.length; i++) {
            RankRealTimeItem rankRealTimeItem = new RankRealTimeItem();
            rankRealTimeItem.setCity(citys[i]);
            rankRealTimeItem.setValue((21 + i * 5) + "");
            listData.add(rankRealTimeItem);
        }
    }
}
