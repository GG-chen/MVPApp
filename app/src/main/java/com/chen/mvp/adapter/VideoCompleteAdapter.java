package com.chen.mvp.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chen.mvp.R;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.video.player.VideoPlayerActivity;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.utils.DefIconFactory;
import com.chen.mvp.utils.DownLoadHelper;
import com.chen.mvp.utils.ImageLoader;
import com.chen.mvp.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by chen on 2017/9/16.
 */

public class VideoCompleteAdapter extends BaseVideoDLAdapter {


    public VideoCompleteAdapter(@LayoutRes int layoutResId, @Nullable List<VideoInfo> data, RxBus rxBus) {
        super(layoutResId, data, rxBus);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final VideoInfo item) {
        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_size, StringUtils.convertStorageNoB(item.getTotalSize()))
                .setText(R.id.tv_title, item.getTitle());

       /*
        // 根据是否为编辑模式来进行处理
        final CheckBox cbDelete = holder.getView(R.id.cb_delete);
        if (mIsEditMode) {
            cbDelete.setVisibility(View.VISIBLE);
            cbDelete.setChecked(mSparseItemChecked.get(holder.getAdapterPosition()));
        } else {
            cbDelete.setVisibility(View.GONE);
            cbDelete.setChecked(false);
        }
        cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //_handleCheckedChanged(holder.getAdapterPosition(), isChecked);
            }
        });

        holder.getView(R.id.tv_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEditMode) {
                    cbDelete.setChecked(!cbDelete.isChecked());
                } else {
                    VideoPlayerActivity.launch(mContext, item);
                }
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mIsEditMode) {
                    cbDelete.setChecked(!cbDelete.isChecked());
                } else {
                    VideoPlayerActivity.launch(mContext, item);
                }*/
                VideoPlayerActivity.launch(mContext, item);
            }
        });
    }




}
