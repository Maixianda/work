package com.gzliangce.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.gzliangce.AppContext;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 监听Home键广播
 */
public class InnerRecevier extends BroadcastReceiver {
    private static Logger logger = LoggerFactory.getLogger(InnerRecevier.class);
    final String SYSTEM_DIALOG_REASON_KEY = "reason";
    final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private Runnable runnable;
    private static IntentFilter intentFilter;
    private static InnerRecevier innerRecevier;


    public static InnerRecevier getInstance() {
        if (innerRecevier == null) {
            innerRecevier = new InnerRecevier();
        }
        return innerRecevier;
    }

    private static IntentFilter getIntentFilter() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        }
        return intentFilter;
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch(Context context) {
        if (innerRecevier != null) {
            context.registerReceiver(innerRecevier, getIntentFilter());
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch(Context context) {
        if (innerRecevier != null) {
            context.unregisterReceiver(innerRecevier);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                Tasks.handler().postDelayed(getRunnable(context), 1000);//按下home键
            }
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Tasks.handler().postDelayed(getRunnable(context), 1000);
        }
    }


    /**
     * 获取跳转线程
     */
    private Runnable getRunnable(Context context) {
        if (runnable == null) {
            runnable = LiangCeUtil.getLockRunnable(context);
        }
        return runnable;
    }
}
