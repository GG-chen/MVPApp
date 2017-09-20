package com.chen.mvp.module.base;

import java.util.List;

/**
 * Created by chen on 2017/9/16.
 */

public interface ILocalView<T> {
    /**
     * 显示数据
     * @param dataList 数据
     */
    void loadData(List<T> dataList);

    /**
     * 没有数据
     */
    void noData();
}
