package com.chen.mvp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chen.mvp.R;
import com.chen.mvp.adapter.item.NewsMultiItem;
import com.chen.mvp.api.NewsUtils;
import com.chen.mvp.api.bean.NewsInfo;
import com.chen.mvp.module.news.PhotoSet.PhotoActivity;
import com.chen.mvp.module.news.article.NewsArticleActivity;
import com.chen.mvp.utils.DefIconFactory;
import com.chen.mvp.utils.ImageLoader;
import com.chen.mvp.utils.ListUtils;
import com.chen.mvp.utils.StringUtils;
import com.chen.mvp.widget.RippleView;
import com.flyco.labelview.LabelView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chen on 2017/9/10.
 * 新闻多布局的Adapter
 */

public class NewsMultiListAdapter extends BaseMultiItemQuickAdapter<NewsMultiItem,BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsMultiListAdapter(List<NewsMultiItem> data) {
        super(data);
        addItemType(NewsMultiItem.ITEM_TYPE_NORMAL, R.layout.adapter_news_list);
        addItemType(NewsMultiItem.ITEM_TYPE_PHOTO_SET, R.layout.adapter_news_photo_set);
    }




    @Override
    protected void convert(BaseViewHolder helper, NewsMultiItem item) {
        switch (item.getItemType()) {
            case NewsMultiItem.ITEM_TYPE_NORMAL:
                handleNewsNormal(helper, item.getNewsBean());
                break;
            case NewsMultiItem.ITEM_TYPE_PHOTO_SET:
                handleNewsPhotoSet(helper, item.getNewsBean());
                break;
        }
    }

    /**
     * 处理正常的新闻
     *
     * @param holder
     * @param item
     */
    private void handleNewsNormal(final BaseViewHolder holder, final NewsInfo item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterCrop(mContext, item.getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        // 设置标签
        if (NewsUtils.isNewsSpecial(item.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        } else {
            holder.setVisible(R.id.label_view, false);
        }
        // 波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (NewsUtils.isNewsSpecial(item.getSkipType())) {
                    //SpecialActivity.launch(mContext, item.getSpecialID());
                } else {
                    // 旧的实现方式和网易的比较相近，感兴趣的可以切换看看
//                    NewsDetailActivity.launch(mContext, item.getPostid());
                    NewsArticleActivity.launch(mContext, item.getPostid());
                }
            }
        });
    }

    /**
     * 处理图片的新闻
     *
     * @param holder
     * @param item
     */
    private void handleNewsPhotoSet(BaseViewHolder holder, final NewsInfo item) {
        ImageView[] newsPhoto = new ImageView[3];
        newsPhoto[0] = holder.getView(R.id.iv_icon_1);
        newsPhoto[1] = holder.getView(R.id.iv_icon_2);
        newsPhoto[2] = holder.getView(R.id.iv_icon_3);
        holder.setVisible(R.id.iv_icon_2, false).setVisible(R.id.iv_icon_3, false);
        ImageLoader.loadCenterCrop(mContext, item.getImgsrc(), newsPhoto[0], DefIconFactory.provideIcon());
        if (!ListUtils.isEmpty(item.getImgextra())) {
            for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
                newsPhoto[i + 1].setVisibility(View.VISIBLE);
                ImageLoader.loadCenterCrop(mContext, item.getImgextra().get(i).getImgsrc(),
                        newsPhoto[i + 1], DefIconFactory.provideIcon());
            }
        }
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());
        // 波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                PhotoActivity.launch(mContext, item.getPhotosetID());
            }
        });
    }

}
