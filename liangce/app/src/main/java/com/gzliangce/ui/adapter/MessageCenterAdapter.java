package com.gzliangce.ui.adapter;

import android.content.Context;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemMessageCenterBinding;
import com.gzliangce.entity.MessageCenterInfo;

import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.date.DateTime;


/**
 * Created by leo on 16/2/24.
 * 消息中心adapter
 */
public class MessageCenterAdapter extends LoadMoreAdapter<MessageCenterInfo, ItemMessageCenterBinding> {

    public MessageCenterAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMessageCenterBinding> vh, int position) {
        vh.getBinding().tvTime.setText(DateTime.formatForM(get(position).getCreateTime()));
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_message_center;
    }
}
