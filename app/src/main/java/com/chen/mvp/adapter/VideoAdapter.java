package com.chen.mvp.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chen.mvp.R;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.module.video.player.VideoPlayerActivity;
import com.chen.mvp.utils.DefIconFactory;
import com.chen.mvp.utils.ImageLoader;

import java.util.List;

/**
 * Created by chen on 2017/9/14.
 */

public class VideoAdapter extends BaseQuickAdapter<VideoInfo,BaseViewHolder> {
    public VideoAdapter(@LayoutRes int layoutResId, @Nullable List<VideoInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final VideoInfo item) {
        final ImageView ivPhoto = holder.getView(R.id.iv_photo);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivPhoto, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayerActivity.launch(mContext, item);
            }
        });
    }
}
