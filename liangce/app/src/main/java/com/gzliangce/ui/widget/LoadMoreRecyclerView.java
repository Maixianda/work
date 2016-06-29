package com.gzliangce.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.gzliangce.ui.adapter.LoadMoreBaseAdapter;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/3/18.
 * LoadMore RecyclerView
 */
public class LoadMoreRecyclerView extends RecyclerView {
    private Logger logger = LoggerFactory.getLogger(LoadMoreRecyclerView.class);
    private LoadMoreBaseAdapter loadMoreAdapter = null;
    private int totalItemCount;
    private int lastVisibleItem;
    private int visibleThreshold = 1;
    private OnRefreshListener onLoadMoreListener;
    private int mScrollState;

    public void setOnLoadMoreListener(OnRefreshListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreRecyclerView(Context context) {
        super(context);
        initView();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        addOnScrollListener(onScrollListener);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof LoadMoreBaseAdapter) {
            loadMoreAdapter = (LoadMoreBaseAdapter) adapter;
        }
        super.setAdapter(adapter);
    }

    public interface OnRefreshListener extends SwipeRefreshLayout.OnRefreshListener {
        void LoadMore();

    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            mScrollState = newState;

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (loadMoreAdapter == null) {
                    return;
                }
                if (!loadMoreAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    logger.e("totalItemCount" + totalItemCount);
                    logger.e(lastVisibleItem + visibleThreshold);
                    if (onLoadMoreListener != null) {
                        loadMoreAdapter.setLoading(true);
                        loadMoreAdapter.startLoading();
                        onLoadMoreListener.LoadMore();
                    }
                }
            }
        }
    };
}
