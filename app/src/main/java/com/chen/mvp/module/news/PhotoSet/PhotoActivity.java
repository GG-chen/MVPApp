package com.chen.mvp.module.news.PhotoSet;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.chen.mvp.R;
import com.chen.mvp.adapter.PhotoSetAdapter;
import com.chen.mvp.api.bean.PhotoSetInfo;
import com.chen.mvp.injector.components.DaggerPhotoSetComponent;
import com.chen.mvp.injector.modules.PhotoSetModule;
import com.chen.mvp.module.base.BaseActivity;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.widget.PhotoViewPager;
import com.dl7.drag.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chen on 2017/9/13.
 */

public class PhotoActivity extends BaseActivity<IBasePresenter> implements IPhotoSetView  {
    private static final String PHOTO_SET_KEY = "PhotoSetKey";

    @BindView(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_index)
    TextView mTvIndex;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;

    private String mPhotoSetId;
    private PhotoSetAdapter mAdapter;
    private List<PhotoSetInfo.PhotosEntity> mPhotosEntities;
    private boolean mIsHideToolbar = false;

    public static void launch(Context context, String mPhotoSetId) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PHOTO_SET_KEY, mPhotoSetId);
        context.startActivity(intent);
    }
    @Override
    public void loadData(PhotoSetInfo photoSetBean) {
        List<String> imgUrls = new ArrayList<>();
        mPhotosEntities = photoSetBean.getPhotos();
        for (PhotoSetInfo.PhotosEntity entity : mPhotosEntities) {
            imgUrls.add(entity.getImgurl());
        }
        mAdapter = new PhotoSetAdapter(this, imgUrls);
        mVpPhoto.setAdapter(mAdapter);
        mVpPhoto.setOffscreenPageLimit(imgUrls.size());

        mTvCount.setText(mPhotosEntities.size() + "");
        mTvTitle.setText(photoSetBean.getSetname());
        mTvIndex.setText(1 + "/");
        mTvContent.setText(mPhotosEntities.get(0).getNote());
        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvContent.setText(mPhotosEntities.get(position).getNote());
                mTvIndex.setText((position + 1) + "/");
            }
        });
        mAdapter.setTapListener(new PhotoSetAdapter.OnTapListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    mDragLayout.scrollOutScreen(300);
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setDuration(300);
                } else {
                    mDragLayout.scrollInScreen(300);
                    mToolbar.animate().translationY(0).setDuration(300);
                }
            }
        });
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, "");
    }

    @Override
    protected void initInjector() {
        mPhotoSetId = getIntent().getStringExtra(PHOTO_SET_KEY);
        DaggerPhotoSetComponent.builder()
                .photoSetModule(new PhotoSetModule(this, mPhotoSetId))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_photo_set;
    }
}
