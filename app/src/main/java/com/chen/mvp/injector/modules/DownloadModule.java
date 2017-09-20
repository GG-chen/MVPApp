package com.chen.mvp.injector.modules;

import com.chen.mvp.adapter.ViewPagerAdapter;
import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.manage.download.DownloadActivity;
import com.chen.mvp.module.manage.download.DownloadPresenter;
import com.chen.mvp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/16.
 */
@Module
public class DownloadModule {

    private final DownloadActivity mView;

    public DownloadModule(DownloadActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }

    @PerActivity
    @Provides
    public IRxBusPresenter provideVideosPresenter(RxBus rxBus) {
        return new DownloadPresenter(rxBus);
    }
}
