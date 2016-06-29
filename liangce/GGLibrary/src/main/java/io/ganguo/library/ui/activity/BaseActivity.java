package io.ganguo.library.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import io.ganguo.library.AppManager;
import io.ganguo.library.R;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;


/**
 * Activity - 基类
 * 用于继承使用
 * <p/>
 * Created by zhihui_chen on 14-8-4.
 */
public abstract class BaseActivity extends Activity implements InitResources {
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register
        AppManager.addActivity(this);
        EventHub.register(this);

        // init resources
        beforeInitView();
        initView();
        initListener();
        initData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // bind back action
        UIHelper.bindActionBack(this, findViewById(R.id.action_back));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregister
        EventHub.unregister(this);
        AppManager.removeActivity(this);
    }
}
