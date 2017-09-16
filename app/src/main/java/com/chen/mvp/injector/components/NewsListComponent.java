package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.injector.modules.NewsListModule;
import com.chen.mvp.module.news.newslist.NewsListFragment;

import dagger.Component;

/**
 * Created by chen on 2017/9/11.
 * 新闻列表注射器
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsListModule.class)
public interface NewsListComponent {
    void inject(NewsListFragment fragment);
}
