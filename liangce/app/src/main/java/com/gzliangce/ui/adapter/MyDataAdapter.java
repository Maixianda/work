package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemMyDataBinding;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.ui.activity.usercenter.DataDocumentActivity;
import com.gzliangce.ui.activity.usercenter.MyOrderDataActivity;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 文档资料
 * Created by zzwdream on 1/21/16.
 */
public class MyDataAdapter extends LoadMoreAdapter<OrderInfo, ItemMyDataBinding> {
    private Logger logger = LoggerFactory.getLogger(MyDataAdapter.class);
    private Activity activity;

    public MyDataAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMyDataBinding> vh, int position) {

    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_my_data;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        switch (view.getId()) {
            case R.id.lly_item_order:
                Intent intent = new Intent(activity, DataDocumentActivity.class);
                intent.putExtra(Constants.ORDER_NUMBER, get(vh.getAdapterPosition()).getNumber());
                activity.startActivity(intent);
                break;
        }
    }
}