package com.chen.mvp.injector.modules;

import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.news.article.NewsArticleActivity;
import com.chen.mvp.module.news.article.NewsArticlePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/13.
 */
@Module
public class NewsArticleModule {
    private final String mNewsId;
    private final NewsArticleActivity mView;

    public NewsArticleModule(NewsArticleActivity view, String newsId) {
        mNewsId = newsId;
        mView = view;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsArticlePresenter(mNewsId, mView);
    }
}
