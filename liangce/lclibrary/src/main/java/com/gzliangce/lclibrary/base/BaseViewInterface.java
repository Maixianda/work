package com.gzliangce.lclibrary.base;

import android.os.Bundle;
import android.view.View;

/**
 * @author fulushan
 * @date 创建时间：2015-06-05 上午9:48:31
 */
public interface BaseViewInterface {

    /**
     * 在初始化控件前
     * @param savedInstanceState
     */
    public void beforeInitView(Bundle savedInstanceState);

    /**
     * 初始化控件
     * @param rootView 根view
     * @param savedInstanceState
     */
    public void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化控件后
     * @param savedInstanceState
     */
    public void afterInitView(Bundle savedInstanceState);

    /**
     * 用户状态切换
     */
    public void userState();


}
