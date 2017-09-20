package com.chen.mvp.module.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chen.mvp.R;
import com.chen.mvp.download.DownloadStatus;
import com.chen.mvp.engine.danmaku.DanmakuConverter;
import com.chen.mvp.engine.danmaku.DanmakuLoader;
import com.chen.mvp.engine.danmaku.DanmakuParser;
import com.chen.mvp.local.table.DanmakuInfo;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.BaseActivity;
import com.chen.mvp.module.manage.download.DownloadActivity;
import com.chen.mvp.utils.CommonConstant;
import com.chen.mvp.utils.DownLoadHelper;
import com.chen.mvp.utils.ToastUtil;
import com.dl7.player.danmaku.OnDanmakuListener;
import com.dl7.player.media.IjkPlayerView;
import com.dl7.player.utils.SoftInputUtils;
import com.dl7.tag.TagView;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.chen.mvp.utils.CommonConstant.VIDEO_DATA_KEY;

/**
 * Created by chen on 2017/9/15.
 */

public class VideoPlayerActivity extends BaseActivity<IVideoPresenter> implements IVideoView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player)
    IjkPlayerView mPlayerView;
    @BindView(R.id.iv_video_download)
    ImageView mIvVideoDownload;
    @BindView(R.id.ll_operate)
    LinearLayout mLlOperate;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ll_edit_layout)
    LinearLayout mLlEditLayout;
    @BindView(R.id.tag_send)
    TagView mTagSend;

    private VideoInfo mVideoData;

    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
    }

    public static void launchForResult(Fragment fragment, VideoInfo data) {
        Intent intent = new Intent(fragment.getContext(), VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        fragment.startActivityForResult(intent, CommonConstant.VIDEO_REQUEST_CODE);
    }

    @Override
    public void loadData(VideoInfo data) {
        /*mVideoData = data;
        mIvVideoDownload.setSelected(data.getDownloadStatus() != DownloadStatus.NORMAL);*/
    }

    @Override
    public void loadDanmakuData(InputStream inputStream) {
        //mPlayerView.setDanmakuSource(inputStream);
    }

    @Override
    protected void updateViews() {
        //mPresenter.getData();
    }

    @Override
    protected void initViews() {
        initToolBar(mToolbar, true, mVideoData.getTitle());
        mPlayerView.init()
                .setTitle(mVideoData.getTitle())
                .setVideoSource(null, mVideoData.getMp4_url(), mVideoData.getMp4Hd_url(), null, null)
                .enableDanmaku()
                .setDanmakuCustomParser(new DanmakuParser(), DanmakuLoader.instance(), DanmakuConverter.instance())
                /*.setDanmakuListener(new OnDanmakuListener<DanmakuInfo>() {
                    @Override
                    public boolean isValid() {
                        return true;
                    }

                    @Override
                    public void onDataObtain(DanmakuInfo danmakuInfo) {
                        Logger.w(danmakuInfo.toString());
                        danmakuInfo.setUserName("Long");
                        danmakuInfo.setVid(mVideoData.getVid());
                        mPresenter.addDanmaku(danmakuInfo);
                    }
                })*/;
        Glide.with(this).load(mVideoData.getCover()).fitCenter().into(mPlayerView.mPlayerThumb);
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPlayerView.editVideo();
                }
                mLlOperate.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            }
        });
        mTagSend.setTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int i, String s, int i1) {
                mPlayerView.sendDanmaku(mEtContent.getText().toString(), false);
                mEtContent.setText("");
                _closeSoftInput();
            }
        });
    }

    @OnClick({R.id.iv_video_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_download:
                /*if (view.isSelected()) {
                    DialogHelper.checkDialog(this, mVideoData);
                } else {
                    DialogHelper.downloadDialog(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DownloaderWrapper.start(mVideoData);
                            mIvVideoDownload.setSelected(true);
                            SnackbarUtils.showDownloadSnackbar(VideoPlayerActivity.this, "任务正在下载", true);
                        }
                    });
                }*/
                DownLoadHelper.start(mVideoData);
                DownloadActivity.launch(this,1);
                ToastUtil.showShortToast("开始下载...");
                break;
        }
    }

    @Override
    protected void initInjector() {
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_video_player;
    }

    private void _closeSoftInput() {
        mEtContent.clearFocus();
        SoftInputUtils.closeSoftInput(this);
        mPlayerView.recoverFromEditVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(CommonConstant.RESULT_KEY, mVideoData.isCollect());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (_isHideSoftInput(view, (int) ev.getX(), (int) ev.getY())) {
            _closeSoftInput();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean _isHideSoftInput(View view, int x, int y) {
        if (view == null || !(view instanceof EditText) || !mEtContent.isFocused()) {
            return false;
        }
        return x < mLlEditLayout.getLeft() ||
                x > mLlEditLayout.getRight() ||
                y > mLlEditLayout.getBottom() ||
                y < mLlEditLayout.getTop();
    }
}
