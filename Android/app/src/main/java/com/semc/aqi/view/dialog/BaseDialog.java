package com.semc.aqi.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.jayfeng.lesscode.core.DisplayLess;

public class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getDecorView().setPadding(DisplayLess.$dp2px(32), DisplayLess.$dp2px(16), DisplayLess.$dp2px(32), DisplayLess.$dp2px(16));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }
}
