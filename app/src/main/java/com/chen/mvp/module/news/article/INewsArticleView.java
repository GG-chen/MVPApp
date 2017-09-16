package com.chen.mvp.module.news.article;

import com.chen.mvp.api.bean.NewsDetailInfo;
import com.chen.mvp.module.base.IBaseView;

/**
 * Created by chen on 2017/9/13.
 */

public interface INewsArticleView extends IBaseView{
    /**
     * 显示数据
     * @param newsDetailBean 新闻详情
     */
    void loadData(NewsDetailInfo newsDetailBean);
}
