package com.chen.mvp.module.manage.download.fragment.complete;

import com.chen.mvp.download.DownloadStatus;
import com.chen.mvp.local.dao.VideoInfoDao;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.ILocalView;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.utils.ListUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by chen on 2017/9/17.
 */

public class VideoCompletePresenter implements IRxBusPresenter {
    private final VideoCompleteFragment mView;
    private final RxBus mRxBus;
    private final VideoInfoDao mDbDao;
    public VideoCompletePresenter(VideoCompleteFragment view, VideoInfoDao videoInfoDao, RxBus rxBus) {
        mView = view;
        mRxBus = rxBus;
        this.mDbDao = videoInfoDao;
    }


    @Override
    public void getData() {
        mDbDao.queryBuilder()
                .where(VideoInfoDao.Properties.DownloadStatus.eq(DownloadStatus.COMPLETE))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoList) {
                        if (ListUtils.isEmpty(videoList)) {
                            mView.noData();
                        } else {
                            mView.loadData(videoList);
                        }
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.getMessage());
            }
        });
        mRxBus.addSubscription(this,subscription);

    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }

    public void deletVideo(VideoInfo info) {
        mDbDao.delete(info);
    }
}
