package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMyOrderBinding;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.fragment.usercenter.AllOrderFragment;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 我的订单界面
 */
public class MyOrderActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(MyOrderActivity.class);
    private ActivityMyOrderBinding binding;
    private AllOrderFragment allOrderFragment;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order);
        setHeader();
    }

    @Override
    public void initView() {
        getSwipeBackLayout().setEdgeSize(100);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        allOrderFragment = AllOrderFragment.getInstance(LiangCeUtil.getUserType(), MyOrderActivity.class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fly_order, allOrderFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        IntentUtil.intentSearchActivity(MyOrderActivity.this, Constants.SEARCH_TYPE[0], new PlaceAnOrder(), null);
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("我的订单");
        header.setRightIcon(R.drawable.ic_search);
        binding.setHeader(header);
    }


}
