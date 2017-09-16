package com.chen.mvp.module.video.main;

/**
 * Created by chen on 2017/9/14.
 */

public interface IVideoMainView {
    /**
     * 更新数据
     * @param lovedCount 收藏数
     */
    void updateLovedCount(int lovedCount);

    /**
     * 更新数据
     * @param downloadCount 下载中个数
     */
    void updateDownloadCount(int downloadCount);
}
