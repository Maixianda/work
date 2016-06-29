package com.gzliangce.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gzliangce.R;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.UiUtil;
import com.leo.swipe.back.base.BaseSwipeBackFragmentActivity;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 15/12/30.
 * FragmentActivity 基类 - 带右滑返回上一页效果
 */
public abstract class BaseSwipeBackFragmentBinding extends BaseSwipeBackFragmentActivity implements HeaderModel.HeaderView {
    public Logger logger = LoggerFactory.getLogger(BaseSwipeBackActivityBinding.class);
    public HeaderModel header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // register
        AppManager.addActivity(this);
        EventHub.register(this);
        Systems.setBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // bind back action
        UiUtil.bindSwipeRefreshView(this, (SwipeRefreshLayout) findViewById(R.id.srv_refresh));
        UIHelper.bindActionBack(this, findViewById(io.ganguo.library.R.id.action_back));
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregister event
        EventHub.unregister(this);
        AppManager.removeActivity(this);
    }
}
