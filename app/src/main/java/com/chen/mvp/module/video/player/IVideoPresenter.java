package com.chen.mvp.module.video.player;

import com.chen.mvp.local.table.DanmakuInfo;
import com.chen.mvp.module.base.IBasePresenter;

/**
 * Created by chen on 2017/9/15.
 */

public interface IVideoPresenter extends IBasePresenter {
    /**
     * 添加一条弹幕到数据库
     * @param danmakuInfo
     */
    void addDanmaku(DanmakuInfo danmakuInfo);

    /**
     * 清空该视频所有缓存弹幕
     */
    void cleanDanmaku();
}
