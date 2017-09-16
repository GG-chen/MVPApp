package com.chen.mvp.injector.components;

import android.content.Context;

import com.chen.mvp.injector.modules.ApplicationModule;
import com.chen.mvp.rxbus.RxBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by chen on 2017/9/7.
 */
@Singleton
@Component (modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context getContext();
    RxBus getRxBus();
}
