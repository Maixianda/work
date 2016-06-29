package io.ganguo.library.ui.adapter.v7;

import android.content.Context;
import android.databinding.ViewDataBinding;

import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.ui.adapter.v7.ViewHolder.LayoutId;


/**
 * Created by rick on 11/28/15.
 */
public class SimpleAdapter<B extends ViewDataBinding> extends ListAdapter<LayoutId, B> {
    public SimpleAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<B> vh, int position) {

    }

    @Override
    protected int getItemLayoutId(int position) {
        return get(position).getItemLayoutId();
    }
}
