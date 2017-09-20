package com.chen.mvp.injector.modules;

import com.chen.mvp.R;
import com.chen.mvp.adapter.BaseVideoDLAdapter;
import com.chen.mvp.adapter.VideoCompleteAdapter;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.local.dao.DaoSession;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.manage.download.fragment.complete.VideoCompleteFragment;
import com.chen.mvp.module.manage.download.fragment.complete.VideoCompletePresenter;
import com.chen.mvp.rxbus.RxBus;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/17.
 */
@Module
public class VideoCompleteModule {
    private final VideoCompleteFragment mView;

    public VideoCompleteModule(VideoCompleteFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePresenter(DaoSession session, RxBus rxBus) {
        return new VideoCompletePresenter(mView, session.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideAdapter(RxBus rxBus) {
        return new VideoCompleteAdapter(R.layout.adapter_video_complete, new ArrayList<VideoInfo>(), rxBus);
    }
}
