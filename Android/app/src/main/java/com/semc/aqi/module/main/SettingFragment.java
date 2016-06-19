package com.semc.aqi.module.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jayfeng.lesscode.core.ToastLess;
import com.jayfeng.lesscode.core.UpdateLess;
import com.jayfeng.lesscode.core.ViewLess;
import com.semc.aqi.R;
import com.semc.aqi.base.BaseFragment;
import com.semc.aqi.model.AppUpdate;
import com.semc.aqi.view.AboutView;
import com.semc.aqi.view.GradeView;
import com.semc.aqi.view.dialog.CommonDialog;

public class SettingFragment extends BaseFragment<SettingContract.Presenter> implements SettingContract.View {

    private RelativeLayout updateContainer;
    private RelativeLayout aboutContainer;
    private RelativeLayout aqiRefContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new SettingPresenter(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        initHeader(rootView);

        updateContainer = ViewLess.$(rootView, R.id.update_container);
        aboutContainer = ViewLess.$(rootView, R.id.about_container);
        aqiRefContainer = ViewLess.$(rootView, R.id.aqi_ref_container);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkUpdate();
            }
        });
        aboutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog aboutDialog = new CommonDialog(getContext());
                aboutDialog.setTitle(getString(R.string.title_activity_about));
                aboutDialog.setContentMode(CommonDialog.CONTENT_MODE_CUSTOM);
                aboutDialog.setCustomView(new AboutView(getContext()));
                aboutDialog.hideBottom();
                aboutDialog.show();
            }
        });
        aqiRefContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog gradeDialog = new CommonDialog(getContext());
                gradeDialog.setTitle("空气质量等级");
                gradeDialog.setContentMode(CommonDialog.CONTENT_MODE_CUSTOM);
                gradeDialog.setCustomView(new GradeView(getContext()));
                gradeDialog.hideBottom();
                gradeDialog.show();
            }
        });
    }

    private void initHeader(View rootView) {
        initHeaderView(rootView, R.string.main_tab_setting_text, false);
        headerView.setTitleColor(getResources().getColor(R.color.global_primary_text_color_white));
    }

    @Override
    public void showCheckUpdateResult(AppUpdate appUpdate) {
        boolean hasUpdate = UpdateLess.$check(getActivity(),
                appUpdate.getVercode(),
                appUpdate.getVername(),
                "http://static.yingyonghui.com/apk/yyh/9999/ac.publish.yyh/30055399/com.yingyonghui.market_2926_30055399.apk"/*appUpdate.getDownload()*/,
                appUpdate.getLog());
        if (!hasUpdate) {
            ToastLess.$(getActivity(), "已经是最新版本");
        }
    }

    @Override
    public void showCheckUpdateError(Throwable e) {
        ToastLess.$(getActivity(), e.toString());
    }
}
