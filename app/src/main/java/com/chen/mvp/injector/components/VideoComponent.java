package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.injector.modules.VideoModule;
import com.chen.mvp.module.video.main.VideosMainFragment;

import dagger.Component;

/**
 * Created by chen on 2017/9/14.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoModule.class)
public interface VideoComponent {
    void inject(VideosMainFragment fragment);
}
