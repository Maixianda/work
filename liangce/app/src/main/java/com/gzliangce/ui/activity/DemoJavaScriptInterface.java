package com.gzliangce.ui.activity;

import android.widget.Toast;

import com.gzliangce.AppContext;

/**
 * Created by aaron on 28/3/16.
 */

public final class DemoJavaScriptInterface {
    DemoJavaScriptInterface() {
    }

    public void clickonAndroid() {
        Toast.makeText(AppContext.me(), "视频开始播放...", Toast.LENGTH_SHORT).show();
    }

    public void endonAndroid() {
        Toast.makeText(AppContext.me(), "视频结束", Toast.LENGTH_SHORT).show();
    }
}
