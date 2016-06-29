package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemSwipeBinding;
import com.gzliangce.entity.OrderProgress;
import com.gzliangce.entity.Progress;
import com.gzliangce.ui.dialog.DeleteDialog;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;


/**
 * Created by aaron on 10/20/15.
 */
public class ProgressSearchAdapter extends ListAdapter<OrderProgress.ConditionEntity, ItemSwipeBinding> {
    private Activity activity;

    public ProgressSearchAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_progress;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemSwipeBinding> vh, int position) {

    }

}
