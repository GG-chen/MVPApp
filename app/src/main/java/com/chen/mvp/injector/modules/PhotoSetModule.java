package com.chen.mvp.injector.modules;

import com.chen.mvp.injector.PerActivity;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.news.PhotoSet.PhotoActivity;
import com.chen.mvp.module.news.PhotoSet.PhotoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chen on 2017/9/13.
 */
@Module
public class PhotoSetModule {
    private final PhotoActivity mView;
    private final String mPhotoSetId;

    public PhotoSetModule(PhotoActivity view, String photoSetId) {
        mView = view;
        mPhotoSetId = photoSetId;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePhotoSetPresenter() {
        return new PhotoPresenter(mView, mPhotoSetId);
    }
}
