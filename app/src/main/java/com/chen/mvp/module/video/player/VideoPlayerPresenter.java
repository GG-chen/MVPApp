package com.chen.mvp.module.video.player;

import com.chen.mvp.local.table.DanmakuInfo;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.rxbus.RxBus;

/**
 * Created by chen on 2017/9/15.
 */

public class VideoPlayerPresenter implements IVideoPresenter {

    private final IVideoView mView;
    private final RxBus mRxBus;
    private final VideoInfo mVideoData;

    public VideoPlayerPresenter(IVideoView view, RxBus rxBus, VideoInfo videoData) {
        mView = view;
        mRxBus = rxBus;
        mVideoData = videoData;
    }
    @Override
    public void getData() {

    }

    @Override
    public void getMoreData() {

    }

    @Override
    public void addDanmaku(DanmakuInfo danmakuInfo) {

    }

    @Override
    public void cleanDanmaku() {

    }
}
