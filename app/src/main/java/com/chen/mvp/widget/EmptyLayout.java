package com.chen.mvp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chen.mvp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen on 2017/9/6.
 */

public class EmptyLayout extends FrameLayout {
    public static final int STATUS_HIDE = 1001;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_NET_ERROR = 2;
    public static final int STATUS_DATA_ERROR = 3;
    public static final int STATUS_OTHER_ERROR = 4;
    private Context mContext;
    private int mCurrentStatus = STATUS_LOADING;
    private int mBgColour;

    @BindView(R.id.empty_layout)
    FrameLayout mEmptyLayout;
    @BindView(R.id.empty_loading_show)
    SpinKitView mEmptyLoading;
    @BindView(R.id.tv_error_msg)
    TextView mEmptyErrorMsg;
    @BindView(R.id.rl_error_bg)
    FrameLayout mEmptyBg;

    private OnRetryListener mOnRetryListener;
    public EmptyLayout(Context context) {

        this(context, null);
    }

    public EmptyLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attributeSet, R.styleable.EmptyLayout);
        try {
            mBgColour = typedArray.getColor(R.styleable.EmptyLayout_background_color, Color.WHITE);
        } finally {
            typedArray.recycle();
        }
        View.inflate(mContext, R.layout.layout_empty_show, this);
        ButterKnife.bind(this);
        mEmptyBg.setBackgroundColor(mBgColour);
        switchEmptyView();
    }

    /**
     * 切换显示界面
     */
    private void switchEmptyView() {
        switch (mCurrentStatus) {
            case STATUS_LOADING:
                setVisibility(VISIBLE);
                mEmptyBg.setVisibility(GONE);
                mEmptyLoading.setVisibility(VISIBLE);
                break;
            case STATUS_HIDE:
                setVisibility(GONE);
                break;
            case STATUS_DATA_ERROR:
            case STATUS_NET_ERROR:
                setVisibility(VISIBLE);
                mEmptyLoading.setVisibility(GONE);
                mEmptyBg.setVisibility(VISIBLE);
                mEmptyErrorMsg.setText("网络数据获取失败，请检查网络是否链接");
                break;
            case STATUS_OTHER_ERROR:
                setVisibility(VISIBLE);
                mEmptyLoading.setVisibility(GONE);
                mEmptyBg.setVisibility(VISIBLE);
                break;

        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        mCurrentStatus = STATUS_HIDE;
        switchEmptyView();
    }

    /**
     * 设置其他错误的消息提示
     * @param msg 错误消息
     */
    public void showOtherError(String msg) {
        mCurrentStatus = STATUS_OTHER_ERROR;
        switchEmptyView();
        mEmptyErrorMsg.setText(msg);
    }

    /**
     * 点击重试
     */
    @OnClick(R.id.tv_error_msg)
    void OnClickError() {
        if (mOnRetryListener != null) {
            mOnRetryListener.onRetry();
        }
    }

    public void setCurrentStatus(@EmptyStatus int status) {
        this.mCurrentStatus = status;
    }
    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }
    public interface OnRetryListener {
        void onRetry();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_LOADING, STATUS_DATA_ERROR, STATUS_NET_ERROR})
    public @interface EmptyStatus{}
}
