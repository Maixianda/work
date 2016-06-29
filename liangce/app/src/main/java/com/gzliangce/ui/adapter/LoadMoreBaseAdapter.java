package com.gzliangce.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.view.ViewGroup;

import io.ganguo.library.core.drawable.MaterialProgressDrawable;
import io.ganguo.library.databinding.IncludeLoadingBinding;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/3/18.
 */
public abstract class LoadMoreBaseAdapter<T, B extends ViewDataBinding> extends LoadMoreAdapter<T, B> {
    private boolean isLoading;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public LoadMoreBaseAdapter(Context context) {
        super(context);
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() != io.ganguo.library.R.layout.include_loading) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return io.ganguo.library.R.layout.include_loading;
        }
        return super.getItemViewType(position);
    }


    public void startLoading() {
        if (loadingView != null) {
            isLoading = true;
            loadingView.start();
        }
    }


    public void stopLoading() {
        if (loadingView != null) {
            loadingView.stop();
            isLoading = false;
        }
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<B> holder) {
        if (holder.getItemViewType() == io.ganguo.library.R.layout.include_loading) {
            if (loadingView != null) {
                loadingView.stop();
            }
        }
        super.onViewDetachedFromWindow(holder);
    }
}

