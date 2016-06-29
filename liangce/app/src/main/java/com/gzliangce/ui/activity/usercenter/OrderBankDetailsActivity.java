package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityOrderDataImageBinding;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.ui.adapter.MyOrderDataAdapter;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.widget.GridSpacingItemDecoration;

/**
 * Created by zzwdream on 1/21/16.
 */
public class OrderBankDetailsActivity extends BaseSwipeBackActivityBinding {
    private ActivityOrderDataImageBinding binding;
    private MyOrderDataAdapter adapter;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_data_image);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new MyOrderDataAdapter(this);
        binding.rvImage.setAdapter(adapter);
        binding.rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvImage.addItemDecoration(new GridSpacingItemDecoration(3, 30, true));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
//        for (int i = 0; i < 10; i++) {
//            OrderInfo orderInfo = new OrderInfo();
//            orderInfo.setOrderType(i);
//            adapter.add(orderInfo);
//        }
//        adapter.notifyDataSetChanged();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("银行明细");
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
