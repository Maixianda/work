package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityOrderDetailsBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.OrderDetailDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.OrderStatusType;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.CancleOrderEvent;
import com.gzliangce.event.RefreshOrderListEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.usercenter.PersonalDetailsActivity;
import com.gzliangce.ui.adapter.MyOrderAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.dialog.ContactsDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import retrofit.Call;

/**
 * Created by leo on 16/1/13.
 * 订单详情
 */
public class OrderDetailsActivity extends BaseSwipeBackActivityBinding {
    private ActivityOrderDetailsBinding binding;
    private int position, itemPosition;
    private OrderInfo orderInfo;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        setHeader();
    }

    @Override
    public void initView() {
        header.onBackClickListener();
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.tvOrderProgressSearch.setOnClickListener(onSingleClickListener);
        binding.tvOrderSignPhoto.setOnClickListener(onSingleClickListener);
        binding.ivBrokePhone.setOnClickListener(onSingleClickListener);
        binding.tvJoinOrder.setOnClickListener(onSingleClickListener);
        binding.tvChangeOrder.setOnClickListener(onSingleClickListener);
        binding.llyActions.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        position = getIntent().getIntExtra(Constants.ORDER_STATUS, 1);
        itemPosition = getIntent().getIntExtra(Constants.ITEM_POSITION, 0);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constants.ORDER_INFO);

        postOrderDetail(orderInfo.getNumber());
        if (position == 4) {
            header.setRightTitle("补充资料");
            header.setRightBackground(R.drawable.ripple_default);
            header.setRightClickable(true);
            binding.setHeader(header);
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("订单详情");
        header.setRightBackground(0);
        header.setRightClickable(false);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onMenuClicked() {
        startActivity(new Intent(this, AdditionalMaterialsActivity.class).putExtra(Constants.ORDER_INFO, orderInfo));
    }


    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_change_order:
                    actionChangeOrderActivity();
                    break;
                case R.id.tv_join_order:
                    actionJoinOrderBtn();
                    break;
                case R.id.tv_order_progress_search:
                    startActivity(new Intent(OrderDetailsActivity.this, ProgressSearchActivity.class).putExtra(Constants.ORDER_NUMBER, orderInfo.getNumber()));
                    break;
                case R.id.tv_order_sign_photo:
                    startActivity(new Intent(OrderDetailsActivity.this, SignNewPhotoActivity.class).putExtra(Constants.ORDER_NUMBER, orderInfo.getNumber()));
                    break;
                case R.id.iv_broke_phone:
                    ContactsDialog contactsDialog = new ContactsDialog(OrderDetailsActivity.this, orderInfo.getAccountPhone(), getMemberId());
                    contactsDialog.show();
                    break;
                case R.id.lly_actions:
                    logger.e(orderInfo.toString());
                    if (LiangCeUtil.judgeUserType(UserType.mediator) || LiangCeUtil.judgeUserType(UserType.simpleUser)) {
                        actionPersonalDetailsActivity(orderInfo.getReceiver(), orderInfo.getStatus());
                    }
                    break;
            }
        }
    };

    /**
     * 获取用户Id
     */
    private String getMemberId() {
        String memberId = "";
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            memberId = orderInfo.getSender() + "";
        } else if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            memberId = orderInfo.getReceiver() + "";
        } else {
            memberId = orderInfo.getReceiver() + "";
        }
        return memberId;
    }

    /**
     * 转单按钮点击事件
     */
    private void actionJoinOrderBtn() {
        if (Strings.isEquals(binding.tvJoinOrder.getText() + "", "评价订单")) {
            actionOrderEvaluationActivity();
        } else if (Strings.isEquals(binding.tvJoinOrder.getText() + "", "取消订单")) {
            showCanCelOrderDialog(true);
        } else {
            showCanCelOrderDialog(false);
        }
    }

    /**
     * 跳转到评价界面
     */
    private void actionOrderEvaluationActivity() {
        Intent intent = new Intent(OrderDetailsActivity.this, OrderEvaluationActivity.class);
        intent.putExtra(Constants.ORDER_NUMBER, orderInfo.getNumber());
        startActivityForResult(intent, 1111);
    }

    /**
     * 跳转到经纪人详情
     */
    private void actionPersonalDetailsActivity(int id, String orderState) {
        Intent intent = new Intent(this, PersonalDetailsActivity.class);
        intent.putExtra(Constants.REQUEST_BROKE_INFO_ID, id);
        intent.putExtra(Constants.INTENT_ORDER_STATE, orderState);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, ButtonStatus.OrderStatusBtn);
        startActivity(intent);
    }

    /**
     * 跳转到转单页面
     */
    private void actionChangeOrderActivity() {
        Intent intent = new Intent(this, ChangeOrderActivity.class);
        intent.putExtra(Constants.ORDER_INFO, orderInfo);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, MyOrderAdapter.class.getName());
        startActivity(intent);
    }

    /**
     * 订单详情
     *
     * @param orderNumber
     */
    private void postOrderDetail(String orderNumber) {
        LoadingHelper.showMaterLoading(this, "加载数据中...");
        Call<OrderDetailDTO> call = ApiUtil.getOrderService().getOrderDetail(orderNumber);
        call.enqueue(new APICallback<OrderDetailDTO>() {
            @Override
            public void onSuccess(OrderDetailDTO orderDetailDTO) {
                orderDetailDTO.getOrderDetail().setCanTransfer(orderInfo.isCanTransfer());
                orderInfo = orderDetailDTO.getOrderDetail();
                binding.setOrderInfo(orderInfo);
                String status = orderInfo.getStatus();
                if (Strings.isEquals(status, OrderStatusType.cancel.toString())
                        && UserType.mortgage == LiangCeUtil.getUserType()) {
                    EventHub.post(new CancleOrderEvent(orderInfo, OrderDetailsActivity.class));
                    ToastHelper.showMessage(getBaseContext(), "中介已经取消该订单");
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(OrderDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }

    /**
     * 订单Dialog
     */
    private void showCanCelOrderDialog(final boolean isCancel) {
        String str = getResources().getText(R.string.Order_cancel_text).toString();
        if (!isCancel) {
            str = getResources().getText(R.string.Order_receiving_text).toString();
        }
        DialogUtil.getMaterialDialog(this, str, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (isCancel) {
                    cancelOrder();
                } else {
                    postReceiveOrder(orderInfo.getNumber());
                }
            }
        }).show();
    }


    /**
     * 取消订单
     */
    private void cancelOrder() {
        Call<BaseDTO> call = ApiUtil.getOrderService().postCancelOrder(orderInfo.getNumber());
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handleData();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(OrderDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                finish();
            }
        });
    }

    private void handleData() {
        EventHub.post(new CancleOrderEvent(orderInfo, OrderDetailsActivity.class));
        ToastHelper.showMessage(OrderDetailsActivity.this, "取消订单成功");
    }

    /**
     * 接单
     *
     * @param orderNumber
     */
    private void postReceiveOrder(final String orderNumber) {
        Call<BaseDTO> call = ApiUtil.getOrderService().postReceiveOrder(orderNumber);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                EventHub.post(new RefreshOrderListEvent(orderNumber, OrderDetailsActivity.class));
                ToastHelper.showMessage(OrderDetailsActivity.this, "接单成功");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(OrderDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            postOrderDetail(orderInfo.getNumber());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
