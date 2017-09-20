package com.chen.mvp.module.manage.download.fragment.complete;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chen.mvp.AndroidApplication;
import com.chen.mvp.R;
import com.chen.mvp.download.DownloadStatus;
import com.chen.mvp.injector.components.DaggerVideoCompleteComponent;
import com.chen.mvp.injector.modules.VideoCompleteModule;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.base.BaseVideoDLFragment;
import com.chen.mvp.module.base.ILocalView;
import com.chen.mvp.module.base.IRxBusPresenter;
import com.chen.mvp.utils.DownLoadHelper;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

/**
 * Created by chen on 2017/9/16.
 */

public class VideoCompleteFragment extends BaseVideoDLFragment<IRxBusPresenter> implements ILocalView<VideoInfo> {
    @BindView(R.id.default_bg)
    TextView mTvBg;
    private VideoInfo swipingVideo;

    @Override
    protected void updateViews() {
        mPresenter.getData();
    }

    @Override
    protected void initViews() {
        mAdapter.openLoadAnimation(SCALEIN);
        mRvVideoList.setLayoutManager(new LinearLayoutManager(mContext));
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRvVideoList);
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.complete_video_contain, true);
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                DownLoadHelper.remote(swipingVideo, true);
                if (mAdapter.getData().size() == 0&& mTvBg.getVisibility() == View.GONE) {
                    mTvBg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                swipingVideo = mAdapter.getItem(pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        mRvVideoList.setAdapter(mAdapter);
        mPresenter.registerRxBus(VideoInfo.class, new Action1<VideoInfo>() {
            @Override
            public void call(VideoInfo info) {
                if (info.getDownloadStatus() == DownloadStatus.COMPLETE) {
                    mAdapter.addData(info);
                    if (mTvBg.getVisibility() == View.VISIBLE) {
                        mTvBg.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    protected void initInjector() {
        DaggerVideoCompleteComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCompleteModule(new VideoCompleteModule(this))
                .build()
                .inject(this);

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_download;
    }

    @Override
    public void loadData(List<VideoInfo> dataList) {
        mAdapter.setNewData(dataList);
    }

    @Override
    public void noData() {
        if (mTvBg.getVisibility() != View.VISIBLE) {
            mTvBg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }
}
