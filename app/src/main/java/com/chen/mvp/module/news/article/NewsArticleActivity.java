package com.chen.mvp.module.news.article;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.mvp.R;
import com.chen.mvp.api.NewsUtils;
import com.chen.mvp.api.bean.NewsDetailInfo;
import com.chen.mvp.injector.components.DaggerNewsArticleComponent;
import com.chen.mvp.injector.modules.NewsArticleModule;
import com.chen.mvp.module.base.BaseSwipeBackActivity;
import com.chen.mvp.module.base.IBasePresenter;
import com.chen.mvp.utils.ListUtils;
import com.chen.mvp.widget.EmptyLayout;
import com.chen.mvp.widget.PullScrollView;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnURLClickListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chen on 2017/9/13.
 */

public class NewsArticleActivity extends BaseSwipeBackActivity<IBasePresenter> implements INewsArticleView {
    private static final String SHOW_POPUP_DETAIL = "ShowPopupDetail";
    private static final String NEWS_ID_KEY = "NewsIdKey";


    @BindView(R.id.ll_pre_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_next_title)
    TextView mTvNextTitle;
    @BindView(R.id.ll_foot_view)
    LinearLayout mLlFootView;
    @BindView(R.id.scroll_view)
    PullScrollView mScrollView;
    @BindView(R.id.empty_layout)
    EmptyLayout mEmptyLayout;


    private int mToolbarHeight;
    private int mTopBarHeight;
    private Animator mTopBarAnimator;
    private int mLastScrollY = 0;
    // 最小触摸滑动距离
    private int mMinScrollSlop;
    private String mNewsId;
    private String mNextNewsId;

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, "");
        mScrollView.setFootView(mLlFootView);
    }

    @Override
    protected void initInjector() {
        mNewsId = getIntent().getStringExtra(NEWS_ID_KEY);
        DaggerNewsArticleComponent.builder()
                .newsArticleModule(new NewsArticleModule(this, mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_news_article;
    }

    @Override
    public void loadData(NewsDetailInfo newsDetailBean) {
        RichText.from(newsDetailBean.getBody())
                .into(mTvContent);
        _handleSpInfo(newsDetailBean.getSpinfo());
        _handleRelatedNews(newsDetailBean);
    }

    /**
     * 处理关联的内容
     *
     * @param spinfo
     */
    private void _handleSpInfo(List<NewsDetailInfo.SpinfoEntity> spinfo) {
        if (!ListUtils.isEmpty(spinfo)) {
            ViewStub stub = (ViewStub) findViewById(R.id.vs_related_content);
            assert stub != null;
            stub.inflate();
            TextView tvType = (TextView) findViewById(R.id.tv_type);
            TextView tvRelatedContent = (TextView) findViewById(R.id.tv_related_content);
            tvType.setText(spinfo.get(0).getSptype());
            RichText.from(spinfo.get(0).getSpcontent())
                    // 这里处理超链接的点击
                    .urlClick(new OnURLClickListener() {
                        @Override
                        public boolean urlClicked(String url) {
                            String newsId = NewsUtils.clipNewsIdFromUrl(url);
                            if (newsId != null) {
                                launch(NewsArticleActivity.this, newsId);
                            }
                            return true;
                        }
                    })
                    .into(tvRelatedContent);
        }
    }

    /**
     * 处理关联新闻
     *
     * @param newsDetailBean
     */
    private void _handleRelatedNews(NewsDetailInfo newsDetailBean) {
        if (ListUtils.isEmpty(newsDetailBean.getRelative_sys())) {
            mTvNextTitle.setText("没有相关文章了");
        } else {
            mNextNewsId = newsDetailBean.getRelative_sys().get(0).getId();
            mTvNextTitle.setText(newsDetailBean.getRelative_sys().get(0).getTitle());
        }
    }
    public static void launch(Context context, String newsId) {
        Intent intent = new Intent(context, NewsArticleActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        context.startActivity(intent);
    }

    private void launchInside(String newsId) {
        Intent intent = new Intent(this, NewsArticleActivity.class);
        intent.putExtra(NEWS_ID_KEY, newsId);
        finish();
        startActivity(intent);
    }
}
