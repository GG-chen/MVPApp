package com.chen.mvp.module.news.main;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.local.table.NewsTypeInfo;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.utils.AssetsHelper;
import com.chen.mvp.utils.GsonHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by chen on 2017/9/7.
 */

public class NewsMainPresenter implements IRxBusPresenter {
    private final INewsMainView mView;
    private final RxBus mRxBus;
    public NewsMainPresenter(NewsMainFragment mView, RxBus rxBus) {
        this.mView = mView;
        this.mRxBus = rxBus;
    }

    @Override
    public void getData() {
        List<NewsTypeInfo> sAllChannels = GsonHelper.convertEntities(AssetsHelper.readData(AndroidApplication.getContext(), "NewsChannel"), NewsTypeInfo.class);
        mView.loadData(sAllChannels);
    }

    @Override
    public void getMoreData() {

    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.toString());
            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }
}
