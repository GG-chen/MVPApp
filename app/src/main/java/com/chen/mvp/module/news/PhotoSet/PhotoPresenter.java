package com.chen.mvp.module.news.PhotoSet;

import com.chen.mvp.api.RetrofitService;
import com.chen.mvp.api.bean.PhotoSetInfo;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.widget.EmptyLayout;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by chen on 2017/9/13.
 */

public class PhotoPresenter implements IBasePresenter {
    private final IPhotoSetView mView;
    private final String mPhotoSetId;

    public PhotoPresenter(IPhotoSetView view, String photoSetId) {
        mView = view;
        mPhotoSetId = photoSetId;
    }

    @Override
    public void getData() {
        RetrofitService.getPhotoSet(mPhotoSetId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<PhotoSetInfo>bindToLife())
                .subscribe(new Subscriber<PhotoSetInfo>() {
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
                    public void onNext(PhotoSetInfo photoSetBean) {
                        mView.loadData(photoSetBean);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
