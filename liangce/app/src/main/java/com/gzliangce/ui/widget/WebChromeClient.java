package com.gzliangce.ui.widget;

import android.webkit.WebView;

/**
 * 监听加载进度
 */
public class WebChromeClient extends android.webkit.WebChromeClient {
    private OnProgressChangedListener onProgressChangedListener;

    public OnProgressChangedListener getOnProgressChangedListener() {
        return onProgressChangedListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public WebChromeClient(OnProgressChangedListener onProgressChangedListener) {
        super();
        this.onProgressChangedListener = onProgressChangedListener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            onProgressChangedListener.onProgressFinish(view.getTitle());
        } else {
            onProgressChangedListener.onProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }



    /**
     * 加载进度监听
     */
    public interface OnProgressChangedListener {

        void onProgress(int newProgress);

        void onProgressFinish(String title);
    }
}