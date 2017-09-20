package com.chen.mvp.module.base;

import android.support.v7.widget.RecyclerView;

import com.chen.mvp.R;
import com.chen.mvp.adapter.BaseVideoDLAdapter;


import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by chen on 2017/9/16.
 */

public abstract class BaseVideoDLFragment<T extends IBasePresenter> extends BaseFragment<T> {

    @BindView(R.id.rv_video_list)
    protected RecyclerView mRvVideoList;

    @Inject
    public BaseVideoDLAdapter mAdapter;
   /*

    *//**
     * 处理后退键
     *
     * @return
     *//*
    public boolean exitEditMode() {
        if (mAdapter.isEditMode()) {
            mAdapter.setEditMode(false);
            mRvVideoList.setPadding(0, 0, 0, 0);
            return true;
        }
        return false;
    }

    *//**
     * 是否存于编辑模式
     * @return
     *//*
    public boolean isEditMode() {
        return mAdapter.isEditMode();
    }

    *//**
     * 全选或取消全选
     * @param isChecked
     *//*
    public void checkAllOrNone(boolean isChecked) {
        mAdapter.checkAllOrNone(isChecked);
    }

    *//**
     * 删除选中
     *//*
    public void deleteChecked() {
        mAdapter.deleteItemChecked();
    }
    */


}
