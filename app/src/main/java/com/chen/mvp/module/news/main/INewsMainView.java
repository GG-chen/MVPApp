package com.chen.mvp.module.news.main;

import com.chen.mvp.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by chen on 2017/9/7.
 */

public interface INewsMainView {

    void loadData(List<NewsTypeInfo> checkList);
}
