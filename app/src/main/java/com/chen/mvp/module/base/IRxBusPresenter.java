package com.chen.mvp.module.base;

import rx.functions.Action1;

/**
 * Created by chen on 2017/9/7.
 */

public interface IRxBusPresenter extends IBasePresenter {
    /**
     * 注册
     * @param eventType
     * @param <T>
     */
    <T> void registerRxBus(Class<T> eventType, Action1<T> action);

    /**
     * 注销
     */
    void unregisterRxBus();
}
