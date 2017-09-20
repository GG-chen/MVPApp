package com.chen.mvp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.rxbus.event.FileEvent;
import com.chen.mvp.rxbus.event.VideoEvent;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by chen on 2017/9/16.
 */

public abstract class BaseVideoDLAdapter extends BaseItemDraggableAdapter<VideoInfo,BaseViewHolder> {

    private static final int INVALID_POS = -1;

    protected boolean mIsEditMode = false;
    protected SparseBooleanArray mSparseItemChecked = new SparseBooleanArray();
    protected RxBus mRxBus = null;

    public BaseVideoDLAdapter(@LayoutRes int layoutResId, @Nullable List<VideoInfo> data,RxBus rxBus) {
        super(layoutResId, data);
        mRxBus = rxBus;
    }

/*
    *//**
     * 处理选中事件
     *
     * @param position
     * @param isChecked
     *//*
    protected void _handleCheckedChanged(int position, boolean isChecked) {
        if (position == INVALID_POS) {
            Logger.i(position + "" + isChecked);
            return;
        }
        mSparseItemChecked.put(position, isChecked);
        int checkedCount = 0;
        int checkedStatus;
        for (int i = 0; i < getItemCount(); i++) {
            if (mSparseItemChecked.get(i, false)) {
                checkedCount++;
            }
        }
        if (checkedCount == 0) {
            checkedStatus = VideoEvent.CHECK_NONE;
        } else if (checkedCount == getItemCount()) {
            checkedStatus = VideoEvent.CHECK_ALL;
        } else {
            checkedStatus = VideoEvent.CHECK_SOME;
        }
        // 通知 DownloadActivity 更新界面
        mRxBus.post(new VideoEvent(checkedStatus));
    }


    public boolean isEditMode() {
        return mIsEditMode;
    }

    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
        if (!mIsEditMode) {
            mSparseItemChecked.clear();
        }
        notifyDataSetChanged();
    }

    *//**
     * 切换 Item 的选中状态
     *
     * @param position
     *//*
    public void toggleItemChecked(int position, BaseViewHolder holder) {
        boolean isChecked = mSparseItemChecked.get(position);
        Logger.d(position + "" + !isChecked);
       // holder.setChecked(R.id.cb_delete, !isChecked);
        _handleCheckedChanged(position, !isChecked);
        // 如果用 notifyItemChanged()，会有一闪的情况
//        notifyItemChanged(position);
    }

    *//*public void deleteItemChecked() {
        for (int i = mSparseItemChecked.size() - 1; i >= 0; i--) {
            if (mSparseItemChecked.valueAt(i)) {
                DownloaderWrapper.delete(getItem(mSparseItemChecked.keyAt(i)));
                removeItem(mSparseItemChecked.keyAt(i));
                mSparseItemChecked.delete(mSparseItemChecked.keyAt(i));
            }
        }
    }*//*

    public void checkAllOrNone(boolean isChecked) {
        for (int i = 0; i < getItemCount(); i++) {
            mSparseItemChecked.put(i, isChecked);
        }
        notifyDataSetChanged();
    }*/

    public void updateDownload(VideoInfo info) {
    }

    /**
     * 移除一条数据
     *
     * @param item 数据
     */
    public void removeItem(VideoInfo item) {
        int pos = 0;
        for (VideoInfo info : mData) {
            if (item.hashCode() == info.hashCode()) {
                try {
                    remove(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            pos++;
        }
    }

    public void onClick(int position) {

    }
}
