package com.gzliangce.ui.activity.setting;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityAboutAppBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;

/**
 * Created by leo on 16/1/13.
 * 关于我们
 */
public class AboutAppActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private ActivityAboutAppBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_app);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.tvCooperation.setOnClickListener(this);
        binding.tvDisclaimer.setOnClickListener(this);
        binding.tvLiangceApp.setOnClickListener(this);
        binding.tvUserAgreement.setOnClickListener(this);
    }

    @Override
    public void initData() {
        header.setMidTitle("关于我们");
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_liangce_app:
                IntentUtil.actionWebActivity(this, "良策金服", 0);
                break;
            case R.id.tv_user_agreement:
                IntentUtil.actionWebActivity(this, "用户协议", 4);
                break;
            case R.id.tv_cooperation:
                IntentUtil.actionWebActivity(this, "战略合作金融机构", 3);
                break;
        }
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
