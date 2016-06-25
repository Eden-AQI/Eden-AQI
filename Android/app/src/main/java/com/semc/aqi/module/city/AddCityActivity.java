package com.semc.aqi.module.city;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.FileLess;
import com.jayfeng.lesscode.core.KeyBoardLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.jayfeng.lesscode.core.other.DividerItemDecoration;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;
import com.semc.aqi.model.CityGroup;
import com.semc.aqi.model.CityGroupList;
import com.semc.aqi.module.main.MainActivity;
import com.semc.aqi.view.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends BaseActivity {

    public static final int GROUP_ID = -1;

    private EditText searchEditView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;
    private DividerItemDecoration dividerItemDecoration;

    private List<CityGroup> originList;
    private List<City> showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        initHeaderView(getTitle().toString(), true);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));

        searchEditView = ViewLess.$(this, R.id.search);
        recyclerView = ViewLess.$(this, R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                new ColorDrawable(getResources().getColor(R.color.global_item_divider_color)));
        dividerItemDecoration.setHeight(1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = searchEditView.getText().toString();
                filterList(key, true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initList();
        filterList("", false);

        adapter = AdapterLess.$recycler(this,
                showList,
                new int[]{R.layout.activity_city_list_item, R.layout.activity_city_list_item_group},
                new AdapterLess.FullRecyclerCallBack<City>() {
                    @Override
                    public void onBindViewHolder(int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, final City city) {
                        View container = recyclerViewHolder.$view(R.id.container);
                        TextView nameView = recyclerViewHolder.$view(R.id.name);
                        nameView.setText(city.getName());

                        if (city.getId() != GROUP_ID) {
                            container.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LiteOrm liteOrm = LiteOrmManager.getLiteOrm(AddCityActivity.this);
                                    QueryBuilder<City> queryBuilder = new QueryBuilder<City>(City.class)
                                            .whereEquals("city_id", city.getId());
                                    boolean already = liteOrm.query(queryBuilder).size() > 0 ? true : false;
                                    if (!already) {
                                        liteOrm.save(city);
                                        EventBus.getDefault().post(new AddCityEvent());

                                        final LoadingDialog loadingDialog = new LoadingDialog(AddCityActivity.this);
                                        loadingDialog.setLoadingText("正在添加...");
                                        loadingDialog.show();
                                        KeyBoardLess.$hide(AddCityActivity.this, searchEditView);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadingDialog.dismiss();
                                                finish();
                                            }
                                        }, 300);
                                    } else {
                                        ToastLess.$(AddCityActivity.this, "已添加");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        City city = showList.get(position);
                        if (city.getId() != GROUP_ID) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });

        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        showList = new ArrayList<>();
        originList = MainActivity.cityGroupList;
    }

    private void filterList(String key, boolean update) {
        showList.clear();
        for (int i = 0; i < originList.size(); i++) {
            CityGroup cityGroup = originList.get(i);
            List<City> groupCities = cityGroup.getItems();
            if (groupCities == null || groupCities.size() == 0) {
                continue;
            }
            // add group
            City groupCity = new City();
            groupCity.setId(GROUP_ID);
            groupCity.setName(cityGroup.getGroupName());
            showList.add(groupCity);

            boolean hasResult = TextUtils.isEmpty(key) ? true : false;
            for (City city : groupCities) {
                String cityName = city.getName();
                if (!TextUtils.isEmpty(cityName) && cityName.contains(key)) {
                    showList.add(city);
                    hasResult = true;
                }
            }
            if (!hasResult) {
                showList.remove(showList.size() - 1);
            }
        }

        if (update) {
            adapter.notifyDataSetChanged();
        }
    }
}
