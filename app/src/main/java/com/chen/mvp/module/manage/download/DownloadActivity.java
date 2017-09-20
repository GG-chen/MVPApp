package com.chen.mvp.module.manage.download;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chen.mvp.R;
import com.chen.mvp.adapter.ViewPagerAdapter;
import com.chen.mvp.injector.components.DaggerDownloadComponent;
import com.chen.mvp.injector.modules.DownloadModule;
import com.chen.mvp.module.base.BaseActivity;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.manage.download.fragment.cache.VideoCacheFragment;
import com.chen.mvp.module.manage.download.fragment.complete.VideoCompleteFragment;
import com.chen.mvp.rxbus.event.VideoEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by chen on 2017/9/16.
 */

public class DownloadActivity extends BaseActivity<IRxBusPresenter> {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.vp_download_container)
    ViewPager mVpDL;
    @BindView(R.id.tab_download_layout)
    TabLayout mTlDL;

    @Inject
    ViewPagerAdapter mAdapter;
    private VideoCompleteFragment mCompleteFragment;
    private VideoCacheFragment mCacheFragment;

    public static void launch(Context context, int page) {
        Intent intent = new Intent(context, DownloadActivity.class);
        if (page == 1) {
            intent.putExtra("page", page);
        }
        context.startActivity(intent);
    }
    @Override
    protected void updateViews() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        mCompleteFragment = new VideoCompleteFragment();
        mCacheFragment = new VideoCacheFragment();
        fragments.add(mCompleteFragment);
        fragments.add(mCacheFragment);
        titles.add("已缓存");
        titles.add("缓存中");
        mAdapter.setItems(fragments, titles);
        mVpDL.setAdapter(mAdapter);
        mTlDL.setupWithViewPager(mVpDL);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar,true,"视频列表");
        mPresenter.registerRxBus(VideoEvent.class, new Action1<VideoEvent>() {
            @Override
            public void call(VideoEvent videoEvent) {

            }
        });
    }

    @Override
    protected void initInjector() {
        DaggerDownloadComponent.builder().applicationComponent(getAppComponent())
                .downloadModule(new DownloadModule(this))
                .build().inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_download;
    }

}
