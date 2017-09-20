package com.chen.mvp.module.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.R;
import com.chen.mvp.injector.components.ApplicationComponent;
import com.chen.mvp.injector.modules.ActivityModule;
import com.chen.mvp.utils.PreferencesUtils;
import com.chen.mvp.widget.EmptyLayout;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chen on 2017/9/6.
 */

public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity implements IBaseView {

    @Nullable
    @BindView(R.id.empty_layout)
    protected EmptyLayout mEmptyLayout;
    /**
     * 把 Presenter 提取到基类需要配合基类的 initInjector() 进行注入，如果继承这个基类则必定要提供一个 Presenter 注入方法，
     * 该APP所有 Presenter 都是在 Module 提供注入实现，也可以选择提供另外不带 Presenter 的基类
     */
    @Inject
    protected T mPresenter;

    @Override
    protected void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        if (!PreferencesUtils.getBoolean(this,"isNight")) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        initInjector();
        initViews();
        updateViews();
    }
    /**
     * 更新视图控件
     */
    protected abstract void updateViews();

    protected abstract void initViews();

    protected abstract void initInjector();

    @LayoutRes
    protected abstract int attachLayoutRes();

    @Override
    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void showError(EmptyLayout.OnRetryListener onRetryListener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_NET_ERROR);
            mEmptyLayout.setOnRetryListener(onRetryListener);
        }
    }

    @Override
    public void showOtherError(String msg, EmptyLayout.OnRetryListener onRetryListener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.showOtherError(msg);
            mEmptyLayout.setOnRetryListener(onRetryListener);
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    protected ApplicationComponent getAppComponent() {
        return AndroidApplication.getAppComponent();
    }

    protected ActivityModule getActivityModle() {
        return new ActivityModule(this);
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    /**
     * 当我们不需要fragment会退到上一个界面或者管理的fragment过多而不想保留BackStackRecord记录过度使用资源时，就可以加入回退栈。
     * @param containerViewId
     * @param fragment
     * @param tag
     */
    protected void addFragment(int containerViewId, Fragment fragment,String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment,tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
