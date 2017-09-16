package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.injector.modules.NewsMainModule;
import com.chen.mvp.module.news.main.NewsMainFragment;

import dagger.Component;

/**
 * Created by chen on 2017/9/9.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsMainModule.class)
public interface NewsMainComponent {
    void inject(NewsMainFragment fragment);
}
