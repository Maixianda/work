package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemBrokeBinding;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.UserType;
import com.gzliangce.ui.activity.usercenter.PersonalDetailsActivity;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.dialog.ContactsDialog;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.ApiDataUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/1/11.
 * 金融经纪列表
 */
public class BrokeAdapter extends LoadMoreAdapter<BrokeInfo, ItemBrokeBinding> {
    private Activity activity;
    private UserType userType;
    private ButtonStatus buttonStatus;
    private PlaceAnOrder placeAnOrder;

    public BrokeAdapter(Activity activity, PlaceAnOrder placeAnOrder) {
        super(activity);
        this.activity = activity;
        userType = LiangCeUtil.getUserType();
        this.placeAnOrder = placeAnOrder;
    }


    public void setButtonStatus(ButtonStatus buttonStatus) {
        this.buttonStatus = buttonStatus;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemBrokeBinding> vh, int position) {
        if (userType == UserType.mediator) {
            vh.getBinding().tvDoOrder.setVisibility(View.VISIBLE);
        } else {
            vh.getBinding().tvDoOrder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_broke;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        switch (view.getId()) {
            case R.id.rly_item_broke:
                actionPersonalDetailsActivity(get(vh.getAdapterPosition()));
                break;
            case R.id.tv_do_order:
                doOrder(vh);
                break;
            case R.id.ibtn_call:
                showContactsDialog(get(vh.getAdapterPosition()));
                break;
        }
    }

    /**
     * 联系金融经纪
     */
    private void showContactsDialog(BrokeInfo info) {
        if (info == null) {
            ToastHelper.showMessage(getContext(), "获取不到联系人数据");
            return;
        }
        ContactsDialog contactsDialog = new ContactsDialog(((Activity) getContext()), info.getPhone(), String.valueOf(info.getAccountId()));
        contactsDialog.show();
    }


    /**
     * 下单
     */
    private void doOrder(BaseViewHolder vh) {
        if (buttonStatus == ButtonStatus.OrderDirectBtn) {
            ApiDataUtil.actionDirectOrder(activity, get(vh.getAdapterPosition()).getAccountId() + "");
        } else {
            showDoPlaceOrderDialog(get(vh.getAdapterPosition()).getAccountId());
        }
    }


    /**
     * 跳转到经纪人详情
     */
    private void actionPersonalDetailsActivity(BrokeInfo info) {
        Intent intent = new Intent(activity, PersonalDetailsActivity.class);
        intent.putExtra(Constants.REQUEST_BROKE_INFO_ID, info.getAccountId());
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, buttonStatus);
        activity.startActivity(intent);
    }

    /**
     * 下单提示框
     *
     * @param accoutId
     */
    private void showDoPlaceOrderDialog(final int accoutId) {
        DialogUtil.getMaterialDialog(activity, getContext().getResources().getString(R.string.Order_place_text), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                placeAnOrder.setMortgageId(accoutId + "");
                ApiDataUtil.doPlaceAnOrder(placeAnOrder, activity);
            }
        }).show();
    }
}
