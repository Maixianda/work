package com.gzliangce.ui.activity.usercenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityMyCollectionBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 我的收藏界面
 */
public class MyCollectionActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(MyCollectionActivity.class);
    private ActivityMyCollectionBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_collection);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.rlyBroker.setOnClickListener(this);
        binding.rlyProduct.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_broker:
                startActivity(new Intent(this, BrokerColletionActivity.class));
                break;
            case R.id.rly_product:
                startActivity(new Intent(this, MyCollectionProductActivity.class));
                break;
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("我的收藏");
        binding.setHeader(header);
    }


}
