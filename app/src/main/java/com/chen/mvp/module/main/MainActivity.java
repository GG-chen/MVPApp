package com.chen.mvp.module.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.chen.mvp.R;
import com.chen.mvp.module.base.BaseActivity;
import com.chen.mvp.module.my.MyFragment;
import com.chen.mvp.module.news.main.NewsMainFragment;
import com.chen.mvp.module.video.main.VideosMainFragment;
import com.chen.mvp.utils.PreferencesUtils;
import com.chen.mvp.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.fl_content)
    FrameLayout mFlContain;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private RxPermissions mRxPermissions;

    private SparseArray<String> mSparseTags = new SparseArray<>();

    @Override
    protected void initViews() {
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "新闻"))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_videos, "视频"))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_my, "我"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        mSparseTags.put(R.id.navigation_news, "News");
        mSparseTags.put(R.id.navigation_videos, "Videos");
        mSparseTags.put(R.id.navigation_my, "my");
        getPermission();
    }

    @Override
    protected void updateViews() {
        replaceFragment(R.id.fl_content, new NewsMainFragment(), "News");
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public void onBackPressed() {
        // 获取堆栈里有几个
        final int stackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackEntryCount == 1) {
            // 如果剩一个说明在主页，提示按两次退出app
            _exit();
        }
    }

    /**
     * 退出
     */
    private long mExitTime = 0;

    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtil.showShortToast("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void getPermission() {
        //TODO


    }
    
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                replaceFragment(R.id.fl_content, new NewsMainFragment(), mSparseTags.get(R.id.navigation_news));
                break;
            case 1:
                replaceFragment(R.id.fl_content, new VideosMainFragment(), mSparseTags.get(R.id.navigation_videos));
                break;
            case 2:
                replaceFragment(R.id.fl_content, new MyFragment(), mSparseTags.get(R.id.navigation_my));
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
