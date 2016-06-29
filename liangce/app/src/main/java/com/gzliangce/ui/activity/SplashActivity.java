package com.gzliangce.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivitySplashBinding;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.ui.activity.BaseActivity;
import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/1/18.
 * 启动欢迎页
 */
public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }


    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LiangCeUtil.isActionLockActivity(SplashActivity.this);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        }, 1000);
    }

}
