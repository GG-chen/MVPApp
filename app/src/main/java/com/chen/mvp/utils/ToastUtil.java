package com.chen.mvp.utils;

import android.content.Context;
import android.widget.Toast;

import com.chen.mvp.AndroidApplication;

/**
 * Created by chen on 2017/8/4.
 */

public class ToastUtil {
    private static Toast mToast;
    private static Context mContext = AndroidApplication.getContext();

    public static void showShortToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();


    }
    public static void showLongToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }


}
