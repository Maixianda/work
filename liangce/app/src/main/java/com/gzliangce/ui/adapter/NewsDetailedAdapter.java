package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemNewsDetailedBinding;
import com.gzliangce.entity.NewsInfo;
import com.gzliangce.ui.activity.NewsDetailedActivity;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.util.IntentUtil;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/3/9.
 * 新闻资讯 - adapter
 */
public class NewsDetailedAdapter extends LoadMoreAdapter<NewsInfo, ItemNewsDetailedBinding> {

    public NewsDetailedAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemNewsDetailedBinding> vh, final int position) {
        PhotoLoader.display(vh.getBinding().ivNews, get(position).getCover(), getContext().getResources().getDrawable(R.drawable.shape_image_loading));
        vh.getBinding().llyActionNews.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (position < size()) {
                    NewsInfo info = get(vh.getAdapterPosition());
                    IntentUtil.actionNewSDetaiActivity(getContext(), info.getTitle(), info.getUrl());
                }
            }
        });
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_news_detailed;
    }
}
