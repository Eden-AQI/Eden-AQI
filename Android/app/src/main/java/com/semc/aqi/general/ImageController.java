package com.semc.aqi.general;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片库统一接口，充当切换器
 */
public class ImageController {

    public static void display(Context context, ImageView view, String url) {
        Glide.with(context).load(url).centerCrop().into(view);
    }

}