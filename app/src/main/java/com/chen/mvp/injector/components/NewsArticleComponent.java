package com.chen.mvp.injector.components;

import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.injector.modules.NewsArticleModule;
import com.chen.mvp.injector.modules.NewsMainModule;
import com.chen.mvp.module.news.article.NewsArticleActivity;

import dagger.Component;
import dagger.Module;

/**
 * Created by chen on 2017/9/13.
 */
@PerActivity
@Component(modules = NewsArticleModule.class)
public interface NewsArticleComponent {
    void inject(NewsArticleActivity activity);
}
