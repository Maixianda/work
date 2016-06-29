package com.gzliangce.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemSimpleUserOrderBinding;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.ui.activity.order.OrderDetailsActivity;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.date.DateTime;

/**
 * Created by leo on 16/2/29.
 * 客户端订单列表
 */
public class SimpleUserOrderAdapter extends LoadMoreAdapter<OrderInfo, ItemSimpleUserOrderBinding> {
    public SimpleUserOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemSimpleUserOrderBinding> vh, int position) {
        setData(vh.getBinding(), get(position));
        setOnclick(vh.getBinding(), position);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_simple_user_order;
    }

    private void setData(ItemSimpleUserOrderBinding binding, OrderInfo orderInfo) {
        binding.tvOrderUser.setText("金融经纪:" + orderInfo.getRealName());
        binding.tvOrderDate.setText(DateTime.formatForM(orderInfo.getCreateTime()));
        binding.tvOrderNumber.setText("案件编号:" + orderInfo.getNumber());
        binding.tvProductType.setText("产品类型:" + orderInfo.getProductName());
    }

    private void setOnclick(final ItemSimpleUserOrderBinding binding, final int position) {
        binding.llyAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_STATUS, 4);//客户端订单列表只会出现已签约的，直接传4
                intent.putExtra(Constants.ORDER_INFO, get(position));
                intent.putExtra(Constants.ITEM_POSITION, position);
                getContext().startActivity(intent);
            }
        });
    }
}
