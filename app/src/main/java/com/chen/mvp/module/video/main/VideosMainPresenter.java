package com.chen.mvp.module.video.main;

import com.chen.mvp.api.RetrofitService;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.base.ILoadDataView;
import com.chen.mvp.widget.EmptyLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;


/**
 * Created by chen on 2017/9/14.
 */

public class VideosMainPresenter implements IBasePresenter {
    final private ILoadDataView mView;
    final private String mVideoId;

    private int mPage = 0;


    public VideosMainPresenter(ILoadDataView view, String videoId) {
        this.mView = view;
        this.mVideoId = videoId;
    }
    @Override
    public void getData() {
        RetrofitService.getVideoList(mVideoId, mPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<VideoInfo>>bindToLife())
                .subscribe(new Subscriber<List<VideoInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onNext(List<VideoInfo> videoList) {
                        mView.loadData(videoList);
                        mPage++;
                    }
                });

    }

    @Override
    public void getMoreData() {
        RetrofitService.getVideoList(mVideoId, mPage)
                .compose(mView.<List<VideoInfo>>bindToLife())
                .subscribe(new Subscriber<List<VideoInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onNext(List<VideoInfo> videoInfos) {
                        mView.loadMoreData(videoInfos);
                        mPage++;
                    }
                });
    }
}
