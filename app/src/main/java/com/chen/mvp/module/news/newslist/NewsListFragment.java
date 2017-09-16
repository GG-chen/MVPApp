package com.chen.mvp.module.news.newslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chen.mvp.R;
import com.chen.mvp.adapter.NewsMultiListAdapter;
import com.chen.mvp.adapter.item.NewsMultiItem;
import com.chen.mvp.api.bean.NewsInfo;
import com.chen.mvp.injector.components.DaggerNewsListComponent;
import com.chen.mvp.injector.modules.NewsListModule;
import com.chen.mvp.module.base.BaseFragment;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.utils.SliderHelper;
import com.daimajia.slider.library.SliderLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by chen on 2017/9/9.
 */

public class NewsListFragment extends BaseFragment<IBasePresenter> implements INewsListView {
    private String mNewsId;
    @BindView(R.id.rv_news_list)
    RecyclerView mRvNewsList;

    @Inject
    BaseQuickAdapter mAdapter;
    private SliderLayout mAdSlider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        }
    }

    @Override
    public void loadAdData(NewsInfo newsBean) {
        View header = View.inflate(mContext, R.layout.header_news_ad, null);
        mAdSlider = (SliderLayout) header.findViewById(R.id.slider_ads);
        SliderHelper.initAdSlider(mContext, mAdSlider, newsBean);
        mAdapter.addHeaderView(header);
    }

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void initViews() {
        mAdapter.setUpFetchEnable(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMoreData();
            }
        },mRvNewsList);
        mAdapter.setPreLoadNumber(4);
        mRvNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvNewsList.addItemDecoration(new DividerItemDecoration(mContext,VERTICAL));
        mRvNewsList.setAdapter(mAdapter);

    }

    @Override
    protected void initInjector() {
        DaggerNewsListComponent.builder()
                .applicationComponent(getAppComponent())
                .newsListModule(new NewsListModule(this, mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    public static Fragment newInstance(String typeId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, typeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void loadData(List<NewsMultiItem> data) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mAdapter.setNewData(data);
    }

    @Override
    public void loadMoreData(List<NewsMultiItem> data) {
        mAdapter.loadMoreComplete();
        mAdapter.addData(data);
    }

    @Override
    public void loadNoData() {
        if (mAdapter.isUpFetching()) {
            mAdapter.setUpFetching(false);
        }
        mAdapter.loadMoreFail();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdSlider != null) {
            mAdSlider.startAutoCycle();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdSlider != null) {
            mAdSlider.startAutoCycle();
        }
    }

}
