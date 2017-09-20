package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.injector.modules.VideoCompleteModule;
import com.chen.mvp.module.manage.download.fragment.complete.VideoCompleteFragment;

import dagger.Component;

/**
 * Created by chen on 2017/9/17.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoCompleteModule.class)
public interface VideoCompleteComponent {
    void inject(VideoCompleteFragment fragment);
}
