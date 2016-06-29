package com.gzliangce.lclibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by Ktoy on 16/6/17.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {


    protected BaseActivity activity;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去标题
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        activity = this;
        context = this;
        beforeInitView(savedInstanceState);
        initView(null,savedInstanceState);
        afterInitView(savedInstanceState);
    }

    protected abstract int getLayoutId();

    @Override
    public void userState() {

    }
}
