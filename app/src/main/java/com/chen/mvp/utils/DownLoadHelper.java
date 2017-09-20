package com.chen.mvp.utils;

import android.content.Context;
import android.text.TextUtils;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.download.DownloadStatus;
import com.chen.mvp.local.dao.VideoInfoDao;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.rxbus.RxBus;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 2017/9/16.
 */

public class DownLoadHelper {
    private static Context mContext;
    private static HashMap<String, VideoInfo> mDLmap = new HashMap<>();
    private static RxBus sRxBus;
    private static VideoInfoDao sDbDao;

    public static void init(Context context, RxBus bus,VideoInfoDao videoInfoDao) {
        sRxBus = bus;
        mContext = context;
        sDbDao = videoInfoDao;
    }

    public static void start(VideoInfo info) {
        if (mDLmap.get(info.getCover()) != null) {
            Logger.d("已在下载列表中！");
            ToastUtil.showShortToast("已在下载列表中！");
            return;
        }

        if (TextUtils.isEmpty(info.getMp4Hd_url())) {
            info.setVideoUrl(info.getMp4_url());
        } else {
            info.setVideoUrl(info.getMp4Hd_url());
        }
        if (checkFileisExit(info)) {
            ToastUtil.showShortToast("视频已存在");
            return;
        }
        process(info);
    }

    private static void process(VideoInfo info) {
        if (!mDLmap.containsKey(info.getCover())) {
            mDLmap.put(info.getCover(), info);
        }
        info.setDownloadStatus(DownloadStatus.WAIT);
        sRxBus.post(info);
        sDbDao.insertOrReplace(info);
        //启动下载
        FileDownloader.getImpl().create(info.getVideoUrl())
                .setPath(getVideoPath(info))
                .setTag(info.getCover())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        VideoInfo info = mDLmap.get(task.getTag());
                        if (info == null) {
                            Logger.d("progress");
                            return;
                        }
                        info.setLoadedSize(soFarBytes);
                        info.setTotalSize(totalBytes);
                        info.setDownloadSpeed(task.getSpeed());
                        info.setDownloadStatus(DownloadStatus.DOWNLOADING);
                        sRxBus.post(info);
                        updateDBInfo(info);

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        VideoInfo info = mDLmap.get(task.getTag());
                        info.setDownloadStatus(DownloadStatus.COMPLETE);
                        sRxBus.post(info);
                        updateDBInfo(info);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        VideoInfo info = mDLmap.get(task.getTag());
                        if (info != null) {
                            info.setDownloadStatus(DownloadStatus.STOP);
                            sRxBus.post(info);
                            updateDBInfo(info);
                        }
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        VideoInfo info = mDLmap.get(task.getTag());
                        info.setDownloadStatus(DownloadStatus.ERROR);
                        sRxBus.post(info);
                        updateDBInfo(info);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        VideoInfo info = mDLmap.get(task.getTag());
                        info.setDownloadStatus(DownloadStatus.ERROR);
                        sRxBus.post(info);
                        updateDBInfo(info);
                    }

                    @Override
                    protected void started(BaseDownloadTask task) {
                        super.started(task);
                        VideoInfo info = mDLmap.get(task.getTag());
                        info.setId(task.getId());
                        info.setDownloadStatus(DownloadStatus.START);
                        sRxBus.post(info);
                        updateDBInfo(info);
                    }
                }).start();
    }

    private static boolean checkFileisExit(VideoInfo info) {
        File file = new File(getVideoPath(info));
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static String getVideoPath(VideoInfo info) {
        return PreferencesUtils.getSavePath(AndroidApplication.getContext()) + File.separator + "video/" + StringUtils.clipFileName(info.getVideoUrl());
    }

    public static void stop(VideoInfo item) {
        FileDownloader.getImpl().pause(item.getId());
    }

    public static void retry(VideoInfo info) {
        process(info);
    }

    public static void remote(VideoInfo item, boolean isComplete) {
        if (!isComplete || item != null) {
            //new File(FileDownloadUtils.getTempPath(getVideoPath(item))).delete();
            //FileDownloader.getImpl().pause(item.getId());
            FileDownloader.getImpl().clear(item.getId(), getVideoPath(item));
        }
        if (mDLmap.containsKey(item.getCover())) {
            mDLmap.remove(item.getCover());
        }
        deleteFile(getVideoPath(item));
        sDbDao.delete(item);
    }

    /**
     * 删除会把下载完成的文件清除
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private static void updateDBInfo(VideoInfo info) {
        sDbDao.update(info);
    }
}
