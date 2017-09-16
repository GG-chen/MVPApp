package com.chen.mvp.module.news.newslist;

import com.chen.mvp.adapter.item.NewsMultiItem;
import com.chen.mvp.api.bean.NewsInfo;
import com.chen.mvp.module.base.ILoadDataView;

import java.util.List;

/**
 * Created by chen on 2017/9/9.
 */

public interface INewsListView extends ILoadDataView<List<NewsMultiItem>> {
    /**
     * 加载广告数据
     * @param newsBean 新闻
     */
    void loadAdData(NewsInfo newsBean);
}
