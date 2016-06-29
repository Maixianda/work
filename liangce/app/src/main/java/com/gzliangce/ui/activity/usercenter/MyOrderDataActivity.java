package com.gzliangce.ui.activity.usercenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityOrderDataBinding;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

import io.ganguo.library.core.event.extend.OnSingleClickListener;

/**
 * Created by zzwdream on 1/21/16.
 */
public class MyOrderDataActivity extends BaseSwipeBackActivityBinding {
    private ActivityOrderDataBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_data);
        binding.llContract.setOnClickListener(onSingleClickListener);
        binding.llBankDetails.setOnClickListener(onSingleClickListener);
        binding.llLoanDetails.setOnClickListener(onSingleClickListener);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("P1500128310000529");
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_contract:
                    doIntent(OrderContractActivity.class);
                    break;
                case R.id.ll_bank_details:
                    doIntent(OrderBankDetailsActivity.class);
                    break;
                case R.id.ll_loan_details:
                    doIntent(OrderLoanDetailsActivity.class);
                    break;
            }
        }
    };

    private void doIntent(Class<?> classValues) {
        startActivity(new Intent(this, classValues));
    }
}
