package io.ganguo.library.ui.adapter.v7;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.ganguo.library.BR;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;


/**
 * Created by rick on 10/20/15.
 */
public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements List<T> {

    private Context mContext;
    private LayoutInflater mInflater;

    public BaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(getInflater(), viewType, parent, false);
        BaseViewHolder<B> vh = new BaseViewHolder<>(binding);
        return vh;
    }

    public abstract void onBindViewBinding(BaseViewHolder<B> vh, int position);


    @Override
    public void onBindViewHolder(BaseViewHolder<B> holder, int position) {
        onBindViewBinding(holder, position);
        holder.getBinding().setVariable(BR.adapter,this);
        holder.bindTo(get(position));
    }


    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    public final String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    public final String getString(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemLayoutId(position);
    }

    protected abstract int getItemLayoutId(int position);

    public final OnItemClickListener getItemClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
                BaseAdapter.this.onItemClick(adapter,vh,view);
            }
        };
    }

    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {

    }

    @BindingAdapter(value = {"itemClick","adapter","vh"}, requireAll = false)
    public static void setOnClick( final View view, final OnItemClickListener command,final BaseAdapter adapter, final BaseViewHolder vh) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.onItemClick(adapter,vh, view);
            }
        });
    }

    @BindingAdapter(value = {"itemSingleClick", "adapter","vh"}, requireAll = false)
    public static void setOnSingleClick(final View view, final OnItemClickListener command,final BaseAdapter adapter,  final BaseViewHolder vh) {
        view.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                command.onItemClick(adapter,vh, view);
            }
        });
    }
}
