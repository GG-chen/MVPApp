package com.chen.mvp.module.news.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.chen.mvp.R;
import com.chen.mvp.adapter.ViewPagerAdapter;
import com.chen.mvp.injector.components.DaggerNewsMainComponent;
import com.chen.mvp.injector.modules.NewsMainModule;
import com.chen.mvp.local.table.NewsTypeInfo;
import com.chen.mvp.module.base.BaseFragment;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.module.manage.download.DownloadActivity;
import com.chen.mvp.module.news.newslist.NewsListFragment;
import com.chen.mvp.utils.ToastUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by chen on 2017/9/7.
 */

public class NewsMainFragment extends BaseFragment<IRxBusPresenter> implements INewsMainView {
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container)
    ViewPager mViewPager;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @Inject
    ViewPagerAdapter mPagerAdapter;
    @Override
    protected void updateViews() {
        mPresenter.getData();
    }


    @Override
    protected void initViews() {
        initToolBar(mToolBar,false,"新闻");
        setHasOptionsMenu(true);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        //打开后的监听
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                ToastUtil.showShortToast(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        //打开前的监听
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }

    @Override
    protected void initInjector() {
        DaggerNewsMainComponent.builder()
                .applicationComponent(getAppComponent())
                .newsMainModule(new NewsMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_main;
    }

    @Override
    public void loadData(List<NewsTypeInfo> checkList) {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (NewsTypeInfo bean : checkList) {
            titles.add(bean.getName());
            fragments.add(NewsListFragment.newInstance(bean.getTypeId()));
        }
        mPagerAdapter.setItems(fragments, titles);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        mSearchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                break;
            case R.id.menu_download:
                DownloadActivity.launch(mContext,0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
