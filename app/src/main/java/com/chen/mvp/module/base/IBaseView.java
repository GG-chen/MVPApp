package com.chen.mvp.module.base;

import com.chen.mvp.widget.EmptyLayout;
import com.trello.rxlifecycle.LifecycleTransformer;


/**
 * Created by chen on 2017/9/6.
 * 基础 BaseView 接口
 */

public interface IBaseView {
    void showLoading();

    void hideLoading();

    /**
     * 设置错误监听
     *@param onRetryListener 监听接口
     */
    void showError(EmptyLayout.OnRetryListener onRetryListener);

    /**
     * 设置其他错误的监听和显示内容
     * @param msg 错误信息
     * @param onRetryListener 监听接口
     */
    void showOtherError(String msg,EmptyLayout.OnRetryListener onRetryListener);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();
}
