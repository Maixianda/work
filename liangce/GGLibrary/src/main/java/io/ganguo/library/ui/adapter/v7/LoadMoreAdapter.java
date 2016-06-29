package io.ganguo.library.ui.adapter.v7;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import io.ganguo.library.R;
import io.ganguo.library.core.drawable.MaterialProgressDrawable;
import io.ganguo.library.databinding.IncludeLoadingBinding;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.Timers;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by rick on 10/23/15.
 */
public abstract class LoadMoreAdapter<T, B extends ViewDataBinding> extends ListAdapter<T, B> {
    private Logger logger = LoggerFactory.getLogger(LoadMoreAdapter.class);
    public LoadingView loadingView;
    private LoadMoreListener mLoadMoreListener;
    private boolean isLastPage = false;
    private Runnable runnable;

    public LoadMoreAdapter(Context context) {
        super(context);
    }

    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.include_loading) {
            IncludeLoadingBinding binding = IncludeLoadingBinding.inflate(getInflater(), parent, false);
            loadingView = new LoadingView(getContext(), binding);
            loadingView.stop();
            return new BaseViewHolder<>(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.include_loading) {
            if (!isLastPage) {
                LoadMoreData(holder);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }


    private void LoadMoreData(BaseViewHolder holder) {
        loadingView.start();
        loadMore();
    }

    @Override
    public int getItemCount() {
        return size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.include_loading;
        }
        return super.getItemViewType(position);
    }

    public void hideLoadMore() {
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public void onFinishLoadMore(boolean lastPage) {
        isLastPage = lastPage;
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public void loadMore() {
        if (mLoadMoreListener != null) {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadMoreListener.onLoadMore();
                }
            }, 500);
        }
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<B> holder) {
        if (holder.getItemViewType() == R.layout.include_loading) {
            loadingView.stop();
        }
        super.onViewDetachedFromWindow(holder);
    }

    public class LoadingView {
        private MaterialProgressDrawable mFooterProgress;
        private Context context;
        private IncludeLoadingBinding binding;
        private boolean isFinish = false;

        public LoadingView(Context context, IncludeLoadingBinding binding) {
            this.context = context;
            this.binding = binding;
        }

        private Context getContext() {
            return context;
        }

        private void initProgressView() {
            mFooterProgress = new MaterialProgressDrawable(getContext(), this.binding.getRoot());
            mFooterProgress.setAlpha(255);
            mFooterProgress.setBackgroundColor(Color.TRANSPARENT);
            Resources resources = getContext().getResources();
            int color = resources.getColor(R.color.colorPrimaryDark);
            int blue = resources.getColor(R.color.colorPrimary);
            int green = resources.getColor(R.color.colorAccent);
            mFooterProgress.setColorSchemeColors(color, blue, green);
        }

        public void start() {
            if (!isFinish) {
                if (mFooterProgress == null) {
                    initProgressView();
                }

                if (!mFooterProgress.isRunning()) {
                    binding.imageView.setImageDrawable(mFooterProgress);
                    mFooterProgress.start();
                }
            }
        }

        public void stop() {
            if (binding.getRoot() != null && mFooterProgress != null && binding.imageView != null) {
                binding.imageView.setImageDrawable(null);
                mFooterProgress.stop();
            }
        }

    }

    public void startRunnable() {
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    onFinishLoadMore(false);
                }
            };
        }
        Timers.setTimeout(runnable, 500);
    }

    public void stopRunnable() {
        if (runnable != null) {
            Timers.killTimer(runnable);
        }
    }

}
