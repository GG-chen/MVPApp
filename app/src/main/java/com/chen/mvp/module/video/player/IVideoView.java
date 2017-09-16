package com.chen.mvp.module.video.player;

import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.IBaseView;

import java.io.InputStream;

/**
 * Created by chen on 2017/9/15.
 */

public interface IVideoView extends IBaseView {
    /**
     * 获取Video数据
     * @param data 数据
     */
    void loadData(VideoInfo data);

    /**
     * 获取弹幕数据
     * @param inputStream 数据
     */
    void loadDanmakuData(InputStream inputStream);
}
