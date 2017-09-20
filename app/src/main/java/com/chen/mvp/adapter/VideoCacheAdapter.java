package com.chen.mvp.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chen.mvp.R;
import com.chen.mvp.api.bean.NewsInfo;
import com.chen.mvp.download.DownloadStatus;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.rxbus.RxBus;
import com.chen.mvp.rxbus.event.FileEvent;
import com.chen.mvp.utils.DefIconFactory;
import com.chen.mvp.utils.DownLoadHelper;
import com.chen.mvp.utils.ImageLoader;
import com.chen.mvp.utils.StringUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/9/17.
 */

public class VideoCacheAdapter extends BaseVideoDLAdapter {


    public VideoCacheAdapter(int adapter_video_cache, ArrayList<VideoInfo> videoInfos, RxBus rxBus) {
        super(adapter_video_cache, videoInfos, rxBus);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoInfo item) {
        holder.itemView.setTag(item.getVideoUrl());
        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle());
        _switchViews(holder, item);

       /* holder.getView(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("btn_download");
                //_handleClick(holder, item);
            }
        });
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
                // 注意这里在切换为编辑状态时，获取holder.getAdapterPosition()=-1 的情况，要在_handleCheckedChanged()进行判断处理
                _handleCheckedChanged(holder.getAdapterPosition(), isChecked);
            }
        });*/

    }

    /**
     * 更新视图
     * @param holder
     * @param item
     */
    private void _switchViews(BaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.DOWNLOADING:
                NumberProgressBar pbDownload = holder.getView(R.id.pb_download);
                if (!(holder.getView(R.id.pb_download).getVisibility() == View.VISIBLE) || !(holder.getView(R.id.btn_download).isSelected())) {
                    holder.setVisible(R.id.pb_download, true)
                            .setText(R.id.tv_total_size, StringUtils.convertStorageNoB(item.getTotalSize()))
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                    holder.getView(R.id.btn_download).setSelected(true);
                    pbDownload.setMax((int) item.getTotalSize());
                }
                holder.setText(R.id.tv_load_size, StringUtils.convertStorageNoB(item.getLoadedSize()) + "/")
                        .setText(R.id.tv_speed, StringUtils.convertStorageNoB(item.getDownloadSpeed()) + "/s");
                pbDownload.setProgress((int) item.getLoadedSize());
                break;
            case DownloadStatus.STOP:
                if (holder.getView(R.id.pb_download).getVisibility() == View.VISIBLE|| holder.getView(R.id.btn_download).isSelected()) {
                    holder.setVisible(R.id.pb_download, false)
                            .setText(R.id.tv_speed, "下载暂停")
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_stop));
                    holder.getView(R.id.btn_download).setSelected(false);
                }
                break;
            case DownloadStatus.COMPLETE:
                //mRxBus.post(item);
            case DownloadStatus.CANCEL:
                // 移除
                removeItem(item);
                break;
            case DownloadStatus.ERROR:
                holder.setText(R.id.tv_speed, "异常出错，请重新下载")
                        .setVisible(R.id.pb_download, false)
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_error));
                holder.getView(R.id.btn_download).setSelected(false);
                break;
        }
    }

    /**
     * 点击处理
     * @param holder
     * @param item
     */
    private void _handleClick(BaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.NORMAL:
            case DownloadStatus.ERROR:
            case DownloadStatus.STOP:
                holder.setText(R.id.tv_speed, "处理中...")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                DownLoadHelper.retry(item);
                break;

            case DownloadStatus.DOWNLOADING:
                holder.setText(R.id.tv_speed, "即将暂停")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                DownLoadHelper.stop(item);
                break;

            default:
                break;
        }
    }

    public void updateDownload(VideoInfo info) {
        VideoInfo videoInfo = findInfo(info);
        if (videoInfo == null) {
            notifyDataSetChanged();
            return;
        }
        info.setId(info.getId());
        info.setDownloadStatus(info.getDownloadStatus());
        info.setTotalSize(info.getTotalSize());
        info.setLoadedSize(info.getLoadedSize());
        info.setDownloadSpeed(info.getDownloadSpeed());
        notifyDataSetChanged();
    }



    private VideoInfo findInfo(VideoInfo info) {
        List<VideoInfo> infos = getData();
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getId() == info.getId()) {
                return infos.get(i);
            }
        }
        //防止删除后再次出现
        if (info.getDownloadStatus() == DownloadStatus.STOP) {
            return null;
        }
        addData(info);
        return null;
    }

    public void deletItem(int pos) {
        DownLoadHelper.remote(getItem(pos),false);
        remove(pos);
    }

    @Override
    public void onClick(int position) {
        VideoInfo info = getItem(position);
        switch (info.getDownloadStatus()) {
            case DownloadStatus.DOWNLOADING:
                DownLoadHelper.stop(info);
                break;
            case DownloadStatus.STOP:
            case DownloadStatus.ERROR:
            case DownloadStatus.WAIT:
                DownLoadHelper.retry(info);
                break;
            default:
                break;
        }
    }

}
