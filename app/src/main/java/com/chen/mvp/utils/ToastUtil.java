package com.chen.mvp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.R;

/**
 * Created by chen on 2017/8/4.
 */

public class ToastUtil {
    private static Toast mToast;
    private static Context mContext = AndroidApplication.getContext();
    private static TextView tv;

    public static void showShortToast(String msg) {
        if (mToast == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View layout = inflater.inflate(R.layout.layout_toast, null);
            tv = layout.findViewById(R.id.toast_tv);
            mToast = new Toast(mContext);
            mToast.setView(layout);
        }
        tv.setText(msg);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();

    }
    public static void showLongToast(String msg) {
        if (mToast == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View layout = inflater.inflate(R.layout.layout_toast, null);
            tv = layout.findViewById(R.id.toast_tv);
            mToast = new Toast(mContext);
            mToast.setView(layout);
        }
        tv.setText(msg);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }


}
