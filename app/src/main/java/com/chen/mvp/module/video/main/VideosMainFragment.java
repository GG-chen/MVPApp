package com.chen.mvp.module.video.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chen.mvp.R;
import com.chen.mvp.injector.components.DaggerVideoComponent;
import com.chen.mvp.injector.modules.VideoModule;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.BaseFragment;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.module.base.ILoadDataView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;

/**
 * Created by chen on 2017/9/14.
 */

public class VideosMainFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<VideoInfo>> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @Inject
    BaseQuickAdapter mAdapter;

    private String mVideoId= "V9LG4E6VR";
    @Override
    public void loadData(List<VideoInfo> data) {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
        mAdapter.setNewData(data);
    }

    @Override
    public void loadMoreData(List<VideoInfo> data) {
        if (mAdapter.isLoading()) {
            mAdapter.loadMoreComplete();
        }
        mAdapter.addData(data);
    }

    @Override
    public void loadNoData() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
        mAdapter.loadMoreFail();
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar,false,"搞笑视频");
        mAdapter.openLoadAnimation(ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mSwipe.isRefreshing()) {
                    return;
                }
                mPresenter.getMoreData();
            }
        });
        mRvPhotoList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvPhotoList.setAdapter(mAdapter);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData();
            }
        });


    }

    @Override
    protected void initInjector() {
        DaggerVideoComponent.builder()
                .applicationComponent(getAppComponent())
                .videoModule(new VideoModule(this, mVideoId))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }
}
