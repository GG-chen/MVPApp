package com.chen.mvp.module.news.PhotoSet;

import com.chen.mvp.api.bean.PhotoSetInfo;
import com.chen.mvp.module.base.IBaseView;

/**
 * Created by chen on 2017/9/13.
 */

public interface IPhotoSetView extends IBaseView {
    /**
     * 显示数据
     * @param photoSetBean 图集
     */
    void loadData(PhotoSetInfo photoSetBean);
}
