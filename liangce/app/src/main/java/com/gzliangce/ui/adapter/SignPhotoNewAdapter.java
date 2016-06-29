package com.gzliangce.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemSignNewAdaperBinding;
import com.gzliangce.entity.SignNewPhotoInfo;
import com.gzliangce.ui.activity.order.SignPhotoDetailsActivity;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/2/27.
 * 签约拍照adapter
 */
public class SignPhotoNewAdapter extends ListAdapter<SignNewPhotoInfo, ItemSignNewAdaperBinding> {
    private String orderNumber;

    public SignPhotoNewAdapter(Context context) {
        super(context);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemSignNewAdaperBinding> vh, final int position) {
        vh.getBinding().llyAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                SignNewPhotoInfo info = get(position);
                info.setPosition(position);
                info.setOrderNumber(orderNumber);
                Intent intent = new Intent(getContext(), SignPhotoDetailsActivity.class);
                intent.putExtra(Constants.SIGN_NEW_PHOTO_INFO, info);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_sign_new_adaper;
    }
}
