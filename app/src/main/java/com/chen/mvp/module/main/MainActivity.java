package com.chen.mvp.module.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chen.mvp.R;
import com.chen.mvp.module.base.BaseActivity;
import com.chen.mvp.module.my.MyFragment;
import com.chen.mvp.module.news.main.NewsMainFragment;
import com.chen.mvp.module.video.main.VideosMainFragment;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout mFlContain;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private RxPermissions mRxPermissions;

    private SparseArray<String> mSparseTags = new SparseArray<>();

    @Override
    protected void initViews() {
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mSparseTags.put(R.id.navigation_news, "News");
        mSparseTags.put(R.id.navigation_videos, "Videos");
        mSparseTags.put(R.id.navigation_my, "my");
        getPermission();
    }

    @Override
    protected void updateViews() {
        addFragment(R.id.fl_content,new NewsMainFragment(),"News");
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    replaceFragment(R.id.fl_content, new NewsMainFragment(), mSparseTags.get(R.id.navigation_news));
                    return true;
                case R.id.navigation_videos:
                    replaceFragment(R.id.fl_content, new VideosMainFragment(), mSparseTags.get(R.id.navigation_videos));
                    return true;
                case R.id.navigation_my:
                    replaceFragment(R.id.fl_content, new MyFragment(), mSparseTags.get(R.id.navigation_my));
                    return true;
            }
            return false;
        }

    };

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
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void getPermission() {
        //TODO


    }
}
