package com.chen.mvp.module.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chen.mvp.R;
import com.chen.mvp.module.manage.setting.SettingActivity;
import com.chen.mvp.utils.ToastUtil;


/**
 * Created by chen on 2017/9/15.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = View.inflate(getActivity(), R.layout.fragment_my, null);
        layout.findViewById(R.id.setting).setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        SettingActivity.launch(getActivity());
    }
}
