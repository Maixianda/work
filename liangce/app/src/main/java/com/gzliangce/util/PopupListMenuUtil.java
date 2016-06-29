package com.gzliangce.util;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gzliangce.databinding.ItemProductPopBinding;
import com.gzliangce.ui.adapter.PopupMenuListAdapter;
import com.gzliangce.ui.callback.ItemCallBack;
import com.gzliangce.ui.callback.OnPopupWindowListener;

/**
 * Created by leo on 16/5/23.
 * 下拉菜单栏 - 复用工具类
 */
public class PopupListMenuUtil<T> {
    private Activity activity;
    private PopupMenuListAdapter adapter;
    private PopupWindow menu;
    private OnPopupWindowListener onPopupWindowListener;
    private ItemCallBack<T> callBack;
    private int layoutId = -1;

    public PopupListMenuUtil(Activity activity, OnPopupWindowListener onPopupWindowListener, int layoutId) {
        this.activity = activity;
        this.layoutId = layoutId;
        this.onPopupWindowListener = onPopupWindowListener;
        initMenuView();
    }

    public void setCallBack(ItemCallBack<T> callBack) {
        this.callBack = callBack;
        getAdapter().setCallBack(callBack);
    }

    public PopupMenuListAdapter getAdapter() {
        return adapter;
    }

    private void initMenuView() {
        ItemProductPopBinding popBinding = ItemProductPopBinding.inflate(activity.getLayoutInflater(), null);
        adapter = getListAdapter();
        popBinding.rvProduct.setLayoutManager(getLayoutManger());
        popBinding.rvProduct.setAdapter(adapter);
        menu = new PopupWindow(popBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        menu.setFocusable(true);
        menu.setOutsideTouchable(true);
        menu.setOnDismissListener(onPopupWindowListener);
        menu.setBackgroundDrawable(new BitmapDrawable());
    }

    public PopupMenuListAdapter getListAdapter() {
        return new PopupMenuListAdapter(activity, callBack, layoutId);
    }

    public RecyclerView.LayoutManager getLayoutManger() {
        return new LinearLayoutManager(activity);
    }


    /**
     * 打开或者关闭菜单
     */
    public void toggleMenu(View mShowView) {
        if (menu.isShowing()) {
            menu.dismiss();
        } else {
            if (onPopupWindowListener != null) {
                onPopupWindowListener.onShow();
            }
            menu.update();
            menu.showAsDropDown(mShowView);
        }
    }
}
