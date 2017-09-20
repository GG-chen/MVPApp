package com.chen.mvp.injector.modules;

import com.chen.mvp.R;
import com.chen.mvp.adapter.BaseVideoDLAdapter;
import com.chen.mvp.adapter.VideoCacheAdapter;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.local.dao.DaoSession;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.manage.download.fragment.cache.VideoCacheFragment;
import com.chen.mvp.module.manage.download.fragment.cache.VideoCachePresenter;
import com.chen.mvp.rxbus.RxBus;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/17.
 */
@Module
public class VideoCacheModule {
    private final VideoCacheFragment mView;

    public VideoCacheModule(VideoCacheFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoCachePresenter(mView,daoSession.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideAdapter(RxBus rxBus) {
        return new VideoCacheAdapter(R.layout.adapter_video_cache,new ArrayList<VideoInfo>(), rxBus);
    }
}
