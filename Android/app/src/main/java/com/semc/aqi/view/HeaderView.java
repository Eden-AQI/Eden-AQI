package com.semc.aqi.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.DrawableLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

public class HeaderView extends RelativeLayout {

    private View containerView;
    private TextView titleView;
    private ImageView backView;
    private TextView backTextView;
    private TextView rightTextView;
    private ImageView rightImageView;
    private View shadowDividerView;

    public HeaderView(Context context) {
        super(context);
        init(null, 0);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_header, this, true);
        containerView = ViewLess.$(this, R.id.header_container);
        titleView = ViewLess.$(this, R.id.title);
        backView = ViewLess.$(this, R.id.back);
        backTextView = ViewLess.$(this, R.id.back_text);
        rightImageView = ViewLess.$(this, R.id.right_image);
        rightTextView = ViewLess.$(this, R.id.right_text);
        shadowDividerView = ViewLess.$(this, R.id.divider);


        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).onBackPressed();
            }
        });
        showLeft(false);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTitleColor(int titlecolor) {
        titleView.setTextColor(titlecolor);
    }

    public void setLeftImage(int leftImage) {
        backView.setImageResource(leftImage);
    }

    public void showLeft(boolean show) {
        backView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            Drawable leftDrawable = DrawableLess.$tint(getResources().getDrawable(R.drawable.header_menu_back),
                    getResources().getColorStateList(R.color.tint_drawable_color));
            backView.setImageDrawable(leftDrawable);
        }
    }

    public void showLeftBackView(boolean show, OnClickListener listener) {
        backView.setOnClickListener(listener);
        backView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showLeftTextView(String text, OnClickListener listener) {
        backTextView.setText(text);
        backTextView.setOnClickListener(listener);
        backTextView.setVisibility(View.VISIBLE);
    }

    public void showRightTextView(String text, OnClickListener listener) {
        rightTextView.setText(text);
        rightTextView.setOnClickListener(listener);
        rightTextView.setVisibility(View.VISIBLE);
    }

    public void showRightTextView(String text, String textColor, OnClickListener listener) {
        rightTextView.setText(text);
        rightTextView.setTextColor(Color.parseColor(textColor));
        rightTextView.setOnClickListener(listener);
        rightTextView.setVisibility(View.VISIBLE);
    }
    public void showRightTextView(String text, int textColor, OnClickListener listener) {
        rightTextView.setText(text);
        rightTextView.setTextColor(textColor);
        rightTextView.setOnClickListener(listener);
        rightTextView.setVisibility(View.VISIBLE);
    }
    public void showRightTextView() {
        rightTextView.setVisibility(View.VISIBLE);
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public void showRightTextView(String text) {
        rightTextView.setText(text);
        rightTextView.setVisibility(View.VISIBLE);
    }

    public void showRightImageView(int drawableId, OnClickListener listener) {
        rightImageView.setImageResource(drawableId);
        rightImageView.setOnClickListener(listener);
        rightImageView.setVisibility(View.VISIBLE);
    }

    public void showTintRightImageView(int drawableId, OnClickListener listener) {
        Drawable tintDrawable = DrawableLess.$tint(getResources().getDrawable(drawableId), getResources().getColorStateList(R.color.tint_drawable_color));
        rightImageView.setImageDrawable(tintDrawable);
        rightImageView.setOnClickListener(listener);
        rightImageView.setVisibility(View.VISIBLE);
    }
    public void showTintRightImageView(int drawableId, OnClickListener listener,int color) {
        Drawable tintDrawable = DrawableLess.$tint(getResources().getDrawable(drawableId), getResources().getColor(color));
        rightImageView.setImageDrawable(tintDrawable);
        rightImageView.setOnClickListener(listener);
        rightImageView.setVisibility(View.VISIBLE);
    }
    public void hideRightTextView() {
        rightTextView.setVisibility(View.GONE);
    }

    public void hideRightImageView() {
        rightImageView.setVisibility(View.GONE);
    }

    public void showRightImageView() {
        rightImageView.setVisibility(View.VISIBLE);
    }

    public void setBgColor(String color) {
        containerView.setBackgroundColor(Color.parseColor(color));
    }

    public void setBgColor(int color) {
        containerView.setBackgroundColor(color);
    }

    public void setBgImage(int bgImage) {
        containerView.setBackgroundResource(bgImage);
    }

    public void alphaShadowDivider(int alpha) {
        shadowDividerView.setAlpha(alpha);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public ImageView getBackView() {
        return backView;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }
}



