package com.gzliangce.ui.callback;

import android.widget.PopupWindow;

/**
 * Created by leo on 16/5/23.
 * PopupWindow 打开关闭监听
 */
public interface OnPopupWindowListener extends PopupWindow.OnDismissListener {
    void onShow();
}
