package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Context;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemMonthsPaymentsBinding;
import com.gzliangce.entity.MonthPaymentslyInfo;

import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/2/17.
 * 月份供款详情
 */
public class PaymentsDetailAdapter extends ListAdapter<MonthPaymentslyInfo, ItemMonthsPaymentsBinding> {
    private Activity activity;

    public PaymentsDetailAdapter(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemMonthsPaymentsBinding> vh, int position) {
        MonthPaymentslyInfo info = get(position);
        vh.getBinding().tvInterest.setText((int) info.getInterest() + "元");
        vh.getBinding().tvPayments.setText((int) info.getPayments() + "元");
        vh.getBinding().tvPrincipal.setText((int) info.getPrincipal() + "元");
        vh.getBinding().tvRemainingLoan.setText((int) info.getRemainingLoan() + "元");
        vh.getBinding().tvMonth.setText((position + 1) + "月");
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_months_payments;
    }
}
