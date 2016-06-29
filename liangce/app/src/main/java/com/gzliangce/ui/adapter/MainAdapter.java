package com.gzliangce.ui.adapter;

import android.app.Activity;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemMainBinding;
import com.gzliangce.entity.AccountInfo;

import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;


/**
 * Created by aaron on 10/20/15.
 */
public class MainAdapter extends ListAdapter<AccountInfo, ItemMainBinding> {
    private Activity activity;

    public MainAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_main;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMainBinding> vh, int position) {

    }
}
