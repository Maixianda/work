package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemMortgageOrderBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.order.ChangeOrderActivity;
import com.gzliangce.ui.activity.order.OrderDetailsActivity;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.fragment.MortgageOrderFragment;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import retrofit.Call;

/**
 * Created by leo on 16/1/23.
 * 按揭首页- adapter
 */
public class MortgageOrderAdapter extends LoadMoreAdapter<OrderInfo, ItemMortgageOrderBinding> {
    private Activity activity;
    private MortgageOrderFragment mortgageOrderFragment;

    public MortgageOrderAdapter(Activity activity, MortgageOrderFragment mortgageOrderFragment) {
        super(activity);
        this.activity = activity;
        this.mortgageOrderFragment = mortgageOrderFragment;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMortgageOrderBinding> vh, int position) {
        vh.getBinding().tvChangeOrder.setSelected(true);
        PhotoLoader.display(vh.getBinding().ivIcon, get(position).getIcon(), getContext().getResources().getDrawable(R.drawable.ic_product_loading));
        vh.getBinding().llAction.setOnClickListener(new OnClickListener(vh));
        if (!get(position).isCanTransfer()) {
            vh.getBinding().tvChangeOrder.setVisibility(View.INVISIBLE);
        } else {
            vh.getBinding().tvChangeOrder.setVisibility(View.VISIBLE);
            vh.getBinding().tvChangeOrder.setOnClickListener(new OnClickListener(vh));
        }
        vh.getBinding().tvJoinOrder.setOnClickListener(new OnClickListener(vh));
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_mortgage_order;
    }

    /**
     * 点击事件监听
     */
    private class OnClickListener extends OnSingleClickListener {
        private BaseViewHolder<ItemMortgageOrderBinding> vh;

        public OnClickListener(BaseViewHolder<ItemMortgageOrderBinding> vh) {
            this.vh = vh;
        }

        @Override
        public void onSingleClick(View v) {
            int position = vh.getAdapterPosition();
            if (position >= size()) {
                return;
            }
            switch (v.getId()) {
                case R.id.ll_action:
                    actionOrderDetails(get(position), OrderDetailsActivity.class, position);
                    break;
                case R.id.tv_change_order:
                    actionOrderDetails(get(position), ChangeOrderActivity.class, position);
                    break;
                case R.id.tv_join_order:
                    showReceiveOrderDialog(get(position).getNumber(), position);
                    break;
            }
        }
    }

    /**
     * 跳转到订单详情
     */
    private void actionOrderDetails(OrderInfo orderInfo, Class mClass, int position) {
        Intent intent = new Intent(getContext(), mClass);
        intent.putExtra(Constants.ORDER_INFO, orderInfo);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, MortgageOrderFragment.class.getName());
        intent.putExtra(Constants.ITEM_POSITION, position);
        getContext().startActivity(intent);
    }

    /**
     * 接单提示Dialog
     */
    private void showReceiveOrderDialog(final String orderNumber, final int position) {
        DialogUtil.getMaterialDialog(activity, getContext().getResources().getString(R.string.Order_receiving_text), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                postReceiveOrder(orderNumber, position);
            }
        }).show();
    }

    /**
     * 根据用户的type调用不同API
     *
     * @param orderNumber
     */
    private void postReceiveOrder(String orderNumber, final int position) {
        Call<BaseDTO> call = ApiUtil.getOrderService().postReceiveOrder(orderNumber);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                notifyItemRemoved(position);
                remove(position);
                AdapterUtil.setHint(MortgageOrderAdapter.this, mortgageOrderFragment.getBinding().llHint);
                ToastHelper.showMessage(activity, "接单成功");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(activity, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }
}
