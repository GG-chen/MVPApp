package com.chen.mvp.injector.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chen.mvp.adapter.NewsMultiListAdapter;
import com.chen.mvp.adapter.item.NewsMultiItem;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.news.newslist.NewsListFragment;
import com.chen.mvp.module.news.newslist.NewsListPresenter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/10.
 */
@Module
public class NewsListModule {

    private final NewsListFragment mNewsListFragment;
    private final String mNewsId;

    public NewsListModule(NewsListFragment mNewsListFragment, String mNewsId) {
        this.mNewsListFragment = mNewsListFragment;
        this.mNewsId = mNewsId;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsListPresenter(mNewsListFragment, mNewsId);

    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new NewsMultiListAdapter(new ArrayList<NewsMultiItem>());
    }
}
