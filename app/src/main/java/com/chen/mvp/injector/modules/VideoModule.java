package com.chen.mvp.injector.modules;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chen.mvp.R;
import com.chen.mvp.adapter.VideoAdapter;
import com.chen.mvp.injector.PerFragment;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.video.main.VideosMainPresenter;
import com.chen.mvp.module.video.main.VideosMainFragment;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/14.
 */
@Module
public class VideoModule {
    private final VideosMainFragment mView;
    private final String mVideoId;

    public VideoModule(VideosMainFragment view, String videoId) {
        this.mView = view;
        this.mVideoId = videoId;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new VideosMainPresenter(mView, mVideoId);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new VideoAdapter(R.layout.adapter_video_list,new ArrayList<VideoInfo>());
    }
}
