package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.injector.modules.DownloadModule;
import com.chen.mvp.module.manage.download.DownloadActivity;

import dagger.Component;

/**
 * Created by chen on 2017/9/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DownloadModule.class)
public interface DownloadComponent {
    void inject(DownloadActivity activity);
}
