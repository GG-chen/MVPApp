package com.chen.mvp.injector.components;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.injector.modules.PhotoSetModule;
import com.chen.mvp.module.news.PhotoSet.PhotoActivity;

import dagger.Component;

/**
 * Created by chen on 2017/9/13.
 */
@PerActivity
@Component(dependencies = AndroidApplication.class,modules = PhotoSetModule.class)
public interface PhotoSetComponent {
    void inject(PhotoActivity activity);
}
