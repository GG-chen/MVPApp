package com.chen.mvp.injector.components;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.injector.modules.VideoCacheModule;
import com.chen.mvp.module.manage.download.fragment.cache.VideoCacheFragment;

import dagger.Component;

/**
 * Created by chen on 2017/9/17.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoCacheModule.class)
public interface VideoCacheComponent {
    void inject(VideoCacheFragment fragment);
}
