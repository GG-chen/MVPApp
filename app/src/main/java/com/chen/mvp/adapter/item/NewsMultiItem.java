package com.chen.mvp.adapter.item;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chen.mvp.api.bean.NewsInfo;

/**
 * Created by chen on 2017/9/10.
 * http://www.jianshu.com/p/b343fcff51b0
 *
 */

public class NewsMultiItem implements MultiItemEntity {
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_PHOTO_SET = 2;
    private int itemType;
    private NewsInfo mNewsBean;

    public NewsMultiItem(int itemType,NewsInfo newsBean) {
        this.itemType = itemType;
        mNewsBean = newsBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public NewsInfo getNewsBean() {
        return mNewsBean;
    }

    public void setNewsBean(NewsInfo mNewsBean) {
        this.mNewsBean = mNewsBean;
    }
}
