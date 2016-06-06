package com.semc.aqi.module.city;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jayfeng.lesscode.core.AdapterLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseActivity;
import com.semc.aqi.event.AddCityEvent;
import com.semc.aqi.event.DeleteCityEvent;
import com.semc.aqi.general.LiteOrmManager;
import com.semc.aqi.model.City;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class CityActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AdapterLess.RecyclerViewHolder> adapter;

    private List<City> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        EventBus.getDefault().register(this);

        initHeaderView(getTitle().toString(), true);
        headerView.setBgColor(Color.parseColor("#000000"));
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
        headerView.showRightImageView(R.drawable.latest_menu_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, AddCityActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = ViewLess.$(this, R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = LiteOrmManager.getLiteOrm(this).query(City.class);

        adapter = AdapterLess.$recycler(this,
                list,
                R.layout.activity_city_list_item,
                new AdapterLess.RecyclerCallBack<City>() {
                    @Override
                    public void onBindViewHolder(final int position, AdapterLess.RecyclerViewHolder recyclerViewHolder, final City city) {
                        View container = recyclerViewHolder.$view(R.id.container);
                        TextView nameView = recyclerViewHolder.$view(R.id.name);
                        nameView.setText(city.getName());

                        container.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CityActivity.this);
                                builder.setMessage("你确定要删除这个城市吗？");
                                builder.setTitle("提示");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        WhereBuilder whereBuilder = new WhereBuilder(City.class, "code = ?", new String[]{list.get(position).getCode() + ""});
                                        LiteOrmManager.getLiteOrm(CityActivity.this).delete(whereBuilder);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(AddCityEvent addCityEvent) {
        list.clear();
        list.addAll(LiteOrmManager.getLiteOrm(this).query(City.class));
        adapter.notifyDataSetChanged();
        finish();
    }
}
