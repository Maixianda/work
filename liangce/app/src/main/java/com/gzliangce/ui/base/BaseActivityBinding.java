package com.gzliangce.ui.base;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gzliangce.R;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.ui.activity.BaseActivity;
import io.ganguo.library.util.Systems;

/**
 * Created by Aaron on 11/10/15.
 * Activity 基类 - 无右滑返回上一页效果
 */
public abstract class BaseActivityBinding extends BaseActivity implements HeaderModel.HeaderView {
    public HeaderModel header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Systems.setBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        UiUtil.bindSwipeRefreshView(this, (SwipeRefreshLayout) findViewById(R.id.srv_refresh));
    }

    @Override
    public void onBackClicked() {
    }

    @Override
    public void onTitleClicked() {

    }

    @Override
    public void onMenuClicked() {
    }


}
