package com.chen.mvp.module.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.R;
import com.chen.mvp.module.main.MainActivity;
import com.chen.mvp.module.manage.setting.SettingActivity;
import com.chen.mvp.utils.PreferencesUtils;
import com.chen.mvp.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by chen on 2017/9/15.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.st_theme)
    Switch mSwitch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = View.inflate(getActivity(), R.layout.fragment_my, null);
        ButterKnife.bind(this,layout);
        layout.findViewById(R.id.setting).setOnClickListener(this);
        if (PreferencesUtils.getBoolean(getActivity(), "isNight")) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }
        return layout;
    }

    @Override
    public void onClick(View view) {
        SettingActivity.launch(getActivity());
    }


    @OnClick(R.id.st_theme)
    void switchTheme() {
        if (mSwitch.isChecked()) {
            PreferencesUtils.putBoolean(AndroidApplication.getContext(),"isNight",true);
        } else {
            PreferencesUtils.putBoolean(AndroidApplication.getContext(),"isNight",false);
        }
        ((MainActivity)getActivity()).recreate();
    }
}
