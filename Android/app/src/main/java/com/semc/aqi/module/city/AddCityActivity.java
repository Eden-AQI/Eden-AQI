package com.semc.aqi.module.city;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;

    private List<City> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        initHeaderView(getTitle().toString(), true);
        headerView.setBgColor(Color.parseColor("#000000"));
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));

        recyclerView = ViewLess.$(this, R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initList();

        adapter = AdapterLess.$recycler(this,
                list,
                R.layout.activity_city_list_item,
                new AdapterLess.RecyclerCallBack<City>() {
                    @Override
                    public void onBindViewHolder(int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, final City city) {
                        View container = recyclerViewHolder.$view(R.id.container);
                        TextView nameView = recyclerViewHolder.$view(R.id.name);
                        nameView.setText(city.getName());

                        container.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LiteOrm liteOrm = LiteOrmManager.getLiteOrm(AddCityActivity.this);
                                boolean already = false;
                                QueryBuilder<City> queryBuilder = new QueryBuilder<City>(City.class)
                                        .whereEquals("code", city.getCode());
                                already = liteOrm.query(queryBuilder).size() > 0 ? true : false;
                                if (!already) {
                                    liteOrm.save(city);
                                    EventBus.getDefault().post(new AddCityEvent());
                                    finish();
                                } else {
                                    ToastLess.$(AddCityActivity.this, "已添加");
                                }
                            }
                        });
                        container.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return true;
                            }
                        });
                    }
                });

        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        list = new ArrayList<>();

        City city = new City("上海", 21);
        list.add(city);

        String[] henanCities = getResources().getStringArray(R.array.henan);
        int[] hennanCityCodes = getResources().getIntArray(R.array.henan_code);
        for (int i = 0; i < henanCities.length; i++) {
            city = new City(henanCities[i], hennanCityCodes[i]);
            list.add(city);
        }
    }
}
