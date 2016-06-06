package com.semc.aqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

public class AqiDetailsItemView extends LinearLayout {

    private TextView detailsItemTitleView;
    private TextView detailsItemTextView;

    public AqiDetailsItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public AqiDetailsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AqiDetailsItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_api_details_item, this, true);
        detailsItemTitleView = ViewLess.$(this, R.id.details_item_title);
        detailsItemTextView = ViewLess.$(this, R.id.details_item_text);
    }

    public void setTitle(String title) {
        detailsItemTitleView.setText(title);
    }

    public void setText(String text) {
        detailsItemTextView.setText(text);
    }
}



