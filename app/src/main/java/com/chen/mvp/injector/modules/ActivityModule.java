package com.chen.mvp.injector.modules;

import android.app.Activity;

import com.chen.mvp.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/7.
 */

@Module
public class ActivityModule {

    private final Activity mActivity;


    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @PerActivity
    @Provides
    Activity getActivity() {
        return mActivity;
    }
}
