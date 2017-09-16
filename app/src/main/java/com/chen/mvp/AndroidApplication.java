package com.chen.mvp;

import android.app.Application;
import android.content.Context;

import com.chen.mvp.api.RetrofitService;
import com.chen.mvp.injector.components.ApplicationComponent;
import com.chen.mvp.injector.components.DaggerApplicationComponent;
import com.chen.mvp.injector.modules.ApplicationModule;
import com.chen.mvp.rxbus.RxBus;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by chen on 2017/9/7.
 */

public class AndroidApplication extends Application{
    private static ApplicationComponent sAppComponent;
    private static Context sContext;
    private RxBus mRxBus = new RxBus();
    private String DB_NAME = "db_news";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initInjector();
        initConfig();

    }

    private void initConfig() {
        /*if (BuildConfig.DEBUG) {
            LeakCanary.install((Application) getApplication());
            Logger.init("LogTAG");
        }*/
        RetrofitService.init();
    }


    private void initInjector() {
        sAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mRxBus))
                .build();
    }

    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }

    public static Context getContext() {
        return sContext;
    }

    public Context getApplication() {
        return sContext;
    }
}
