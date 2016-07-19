package com.semc.aqi.view.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;

// global common dialog
public class CommonDialog extends BaseDialog {

    public static final int CONTENT_MODE_CUSTOM = -1;      // customize the content
    public static final int CONTENT_MODE_TEXT = 0;         // default
    public static final int CONTENT_MODE_EDIT = 1;         // edit text dialog
    public static final int CONTENT_MODE_LIST = 2;         // list dialog

    public int contentMode = CONTENT_MODE_TEXT;

    protected TextView titleView;
    protected Button confirmButton;
    protected Button cancelButton;
    protected TextView contentView;
    protected EditText editTextView;
    protected View buttonDividerView;
    protected View closeView;
    protected View iconView;

    protected View bottomDividerView;
    protected View bottomContainer;

    protected RelativeLayout customContainer;
    protected LinearLayout textContainer;
    protected LinearLayout listContainer;
    protected LinearLayout editContainer;

    public CommonDialog(Context context) {
        super(context);
        init();
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    /**
     * *******************************************************************
     * DEFAULT TEXT DIALOG
     * *******************************************************************
     */
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_common);

        titleView = ViewLess.$(this, R.id.title);
        confirmButton = ViewLess.$(this, R.id.confirm);
        cancelButton = ViewLess.$(this, R.id.cancel);
        contentView = ViewLess.$(this, R.id.content);
        editTextView = ViewLess.$(this, R.id.edit);
        buttonDividerView = ViewLess.$(this, R.id.button_divider);
        closeView = ViewLess.$(this, R.id.close);
        iconView = ViewLess.$(this, R.id.icon);

        bottomDividerView = ViewLess.$(this, R.id.bottom_divider);
        bottomContainer = ViewLess.$(this, R.id.bottom_container);

        customContainer = ViewLess.$(this, R.id.custom_container);
        textContainer = ViewLess.$(this, R.id.text_container);
        listContainer = ViewLess.$(this, R.id.list_container);
        editContainer = ViewLess.$(this, R.id.edit_container);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setTitle(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    public void setContent(String content) {
        contentView.setText(content);
    }

    public void setContentColor(int color) {
        contentView.setTextColor(color);
    }

    public void setConfirmText(String text) {
        if (confirmButton != null) {
            confirmButton.setText(text);
        }
    }

    public void setCancelText(String text) {
        if (cancelButton != null) {
            cancelButton.setText(text);
        }
    }


    public void setConfirmOnClickListener(View.OnClickListener confirmOnClickListener) {
        if (confirmButton != null && confirmOnClickListener != null) {
            confirmButton.setOnClickListener(confirmOnClickListener);
        }
    }

    public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
        if (cancelButton != null && cancelOnClickListener != null) {
            cancelButton.setOnClickListener(cancelOnClickListener);
        }
    }


    /**
     * *******************************************************************
     * SETTING DIALOG STYLE
     * *******************************************************************
     */
    public void hideTitle() {
        titleView.setVisibility(View.GONE);
        textContainer.setBackgroundResource(R.drawable.view_dialog_top_shape);
    }

    public void hideBottom() {
        bottomContainer.setVisibility(View.GONE);
        bottomDividerView.setVisibility(View.GONE);
    }

    public void hideCancelButton() {
        cancelButton.setVisibility(View.GONE);
        buttonDividerView.setVisibility(View.GONE);
        confirmButton.setBackgroundResource(R.drawable.view_dialog_bottom);
    }

    public void hideConfirmButton() {
        confirmButton.setVisibility(View.GONE);
        buttonDividerView.setVisibility(View.GONE);
        cancelButton.setBackgroundResource(R.drawable.view_dialog_bottom);
    }

    public void setConfirmTextColor(int color) {
        confirmButton.setTextColor(color);
    }

    public void setCancelTextColor(int color) {
        cancelButton.setTextColor(color);
    }

    public void setConfirmButtonBackground(int backgroundResource) {
        confirmButton.setBackgroundResource(backgroundResource);
    }

    public void setCancelButtonBackground(int backgroundResource) {
        cancelButton.setBackgroundResource(backgroundResource);
    }

    public void showClose(boolean isShow, View.OnClickListener clickListener) {
        closeView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (clickListener != null) {
            closeView.setOnClickListener(clickListener);
        }
    }

    /**
     * *******************************************************************
     * CONTENT MODE
     * *******************************************************************
     */
    public void setContentMode(int mode) {
        contentMode = mode;

        if (contentMode == CONTENT_MODE_EDIT) {
            textContainer.setVisibility(View.GONE);
            editContainer.setVisibility(View.VISIBLE);
        } else if (contentMode == CONTENT_MODE_CUSTOM) {
            textContainer.setVisibility(View.GONE);
            customContainer.setVisibility(View.VISIBLE);
        }
    }

    // LIST DIALOG
    public void addItem(String text, View.OnClickListener clickListener) {
        TextView itemView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_dialog_common_list_item, null);
        itemView.setText(text);
        itemView.setOnClickListener(clickListener);
        listContainer.addView(itemView);

        // set mode to list
        listContainer.setVisibility(View.VISIBLE);
        textContainer.setVisibility(View.GONE);
    }

    public void addListDivider() {
        View dividerView = new View(getContext());
        dividerView.setBackgroundColor(getContext().getResources().getColor(R.color.global_content_bg_color));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dividerView.setLayoutParams(params);
        listContainer.addView(dividerView);
    }

    // EDIT DIALOG
    public String getEditValue() {
        return editTextView.getText().toString();
    }

    public void setEditValue(String value) {
        editTextView.setText(value);
        Editable etext = editTextView.getText();
        Selection.setSelection(etext, etext.length());
    }

    public void setMaxLength(int max) {
        editTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    // CUSTOM DIALOG
    public void setCustomView(View customView) {
        customContainer.removeAllViews();
        customContainer.addView(customView);
    }
}
