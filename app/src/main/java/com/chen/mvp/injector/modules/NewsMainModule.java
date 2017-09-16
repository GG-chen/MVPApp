package com.chen.mvp.injector.modules;

import com.chen.mvp.adapter.ViewPagerAdapter;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.news.main.NewsMainFragment;
import com.chen.mvp.module.news.main.NewsMainPresenter;
import com.chen.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/7.
 */
@Module
public class NewsMainModule {
    private final NewsMainFragment mView;

    public NewsMainModule(NewsMainFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideMainPresenter(RxBus rxBus) {
        return new NewsMainPresenter(mView, rxBus);
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }
}
