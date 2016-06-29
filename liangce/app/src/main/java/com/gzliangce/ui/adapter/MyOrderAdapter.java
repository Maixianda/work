package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemMyOrderBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.event.CancleOrderEvent;
import com.gzliangce.event.RefreshOrderListEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.order.ChangeOrderActivity;
import com.gzliangce.ui.activity.order.OrderDetailsActivity;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.fragment.usercenter.MyOrderFragment;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/1/11.
 * 订单相关adapter
 */
public class MyOrderAdapter extends LoadMoreAdapter<OrderInfo, ItemMyOrderBinding> {
    private Logger logger = LoggerFactory.getLogger(MyOrderAdapter.class);
    private Activity activity;
    private int position;
    private MyOrderFragment myOrderFragment;
    private Class mClass;

    public MyOrderAdapter(Activity activity, int position, Class mClass) {
        super(activity);
        this.activity = activity;
        this.position = position;
        this.mClass = mClass;
    }

    public MyOrderFragment getMyOrderFragment() {
        return myOrderFragment;
    }

    public void setMyOrderFragment(MyOrderFragment myOrderFragment) {
        this.myOrderFragment = myOrderFragment;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMyOrderBinding> vh, int position) {
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_my_order;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        if (vh.getAdapterPosition() >= size()) {
            return;
        }
        switch (view.getId()) {
            case R.id.lly_item_order:
                Intent intent = new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_STATUS, position);
                intent.putExtra(Constants.ORDER_INFO, get(vh.getAdapterPosition()));
                intent.putExtra(Constants.ITEM_POSITION, vh.getAdapterPosition());
                activity.startActivity(intent);
                break;
            case R.id.tv_cancel_order:
                showCancelOrderDialog(get(vh.getAdapterPosition()), "确定取消订单吗？", vh.getAdapterPosition(), R.id.tv_cancel_order);
                break;
            case R.id.tv_join_order:
                showCancelOrderDialog(get(vh.getAdapterPosition()), "确定接单吗？", vh.getAdapterPosition(), R.id.tv_join_order);
                break;
            case R.id.tv_change_order:
                showCancelOrderDialog(get(vh.getAdapterPosition()), "确定转单吗？", vh.getAdapterPosition(), R.id.tv_change_order);
                break;
        }
    }

    /**
     * 订单操作提示弹窗
     *
     * @param orderInfo
     * @param position
     */
    private void showCancelOrderDialog(final OrderInfo orderInfo, String hint, final int position, final int id) {
        if (orderInfo == null) {
            return;
        }
        DialogUtil.getMaterialDialog(activity, hint, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (id) {
                    case R.id.tv_cancel_order:
                        cancelOrder(orderInfo.getNumber(), position);
                        break;
                    case R.id.tv_join_order:
                        postReceiveOrder(orderInfo.getNumber(), position);
                        break;
                    case R.id.tv_change_order:
                        actionOrderDetails(orderInfo, ChangeOrderActivity.class);
                        break;
                }
            }
        }).show();
    }


    /**
     * 取消订单
     *
     * @param orderNumber
     * @param position
     */
    private void cancelOrder(String orderNumber, final int position) {
        Call<BaseDTO> call = ApiUtil.getOrderService().postCancelOrder(orderNumber);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handleData(position);
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

    private void handleData(int position) {
        EventHub.post(new CancleOrderEvent(get(position), mClass));
        notifyItemRemoved(position);
        remove(position);
        if (myOrderFragment != null) {
            myOrderFragment.setHint();
        }
        ToastHelper.showMessage(activity, "取消订单成功");
    }

    /**
     * 接单
     *
     * @param orderNumber
     */
    private void postReceiveOrder(final String orderNumber, final int position) {
        Call<BaseDTO> call = ApiUtil.getOrderService().postReceiveOrder(orderNumber);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                ToastHelper.showMessage(activity, "接单成功");
                notifyItemRemoved(position);
                remove(position);
                EventHub.post(new RefreshOrderListEvent(orderNumber, mClass));
                if (myOrderFragment != null) {
                    myOrderFragment.setHint();
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(activity, message);
                if (myOrderFragment != null) {
                    myOrderFragment.setHint();
                }
            }

            @Override
            public void onFinish() {
            }
        });
    }

    /**
     * 跳转到转单
     */
    private void actionOrderDetails(OrderInfo orderInfo, Class mClass) {
        Intent intent = new Intent(getContext(), mClass);
        intent.putExtra(Constants.ORDER_INFO, orderInfo);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, MyOrderAdapter.class.getName());
        getContext().startActivity(intent);
    }
}
