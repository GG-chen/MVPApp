package com.chen.mvp.injector.modules;

import android.content.Context;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.local.dao.DaoSession;
import com.chen.mvp.rxbus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/7.
 */
@Module
public class ApplicationModule {

    private final AndroidApplication mApplication;
    private final DaoSession mDaoSession;
    private final RxBus mRxBus;

    public ApplicationModule(AndroidApplication mApplication, DaoSession mDaoSession, RxBus mRxbus) {
        this.mApplication = mApplication;
        this.mRxBus = mRxbus;
        this.mDaoSession = mDaoSession;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication.getApplication();
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return mRxBus;
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        return mDaoSession;
    }

}
