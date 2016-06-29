package com.gzliangce.lclibrary.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzliangce.lclibrary.R;
import com.gzliangce.lclibrary.base.BaseWebActivity;
import com.gzliangce.lclibrary.views.VDHLayout;

/**
 * 带悬浮按钮activity
 */
public class MenuBtnWebActivity extends BaseWebActivity implements View.OnClickListener {

    VDHLayout vdh;
    TextView menuBtn;
    String javascript;
   public final static String JAVASCRIPT_KEY="JAVASCRIPT_KEY";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_menubtn_web;
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        super.initView(rootView, savedInstanceState);
        javascript = getIntent().getStringExtra(JAVASCRIPT_KEY);
        vdh = (VDHLayout) findViewById(R.id.id_vdh);
        menuBtn = (TextView) findViewById(R.id.id_menu_btn);
        menuBtn.setOnClickListener(this);
    }

    @Override
    public void startLoading() {
        vdh.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
    }

    @Override
    public void loadSuccess() {
        vdh.setVisibility(View.VISIBLE);
        menuBtn.setVisibility(View.VISIBLE);
//        idWebWebView.loadUrl("javascript:(function(){\n" +
//                "document.getElementById(\"token\").value = '+"+name+"';document.getElementById(\"deviceId\").value='+"+name+"+';" +
//                "})()");

//        idWebWebView.loadUrl("javascript:(function(){\n" +
//                "document.getElementById(\"tokenId\").value =' " + name +"'"+
//                "})()");
        if(null!=javascript||!javascript.trim().isEmpty()){
            Log.e("ceshi-:","javascript:   "+javascript);
//            loadJS=true;
//            idWebWebView.loadUrl("javascript:(function(){\n" +
//                    "document.getElementById(\"tokenId\").value =' " + "测试2" + "'" +
//                    "})()");
            idWebWebView.loadUrl(javascript);
        }
    }

    @Override
    public void loadFailure() {
        vdh.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
