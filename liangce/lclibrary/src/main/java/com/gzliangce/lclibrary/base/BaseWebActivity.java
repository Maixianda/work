package com.gzliangce.lclibrary.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gzliangce.lclibrary.R;
import com.gzliangce.lclibrary.views.LoadingLayout;


public class BaseWebActivity extends BaseActivity {

    private boolean isLoading;

    private String url;

    public static final String WEB_URL = "url";


    public WebView idWebWebView;
    LoadingLayout idWebLoadLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_web;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(WEB_URL);
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        idWebWebView = (WebView) findViewById(R.id.id_webview);
        idWebLoadLayout = (LoadingLayout) findViewById(R.id.id_loadview);
    }


    @Override
    public void afterInitView(Bundle savedInstanceState) {
        if (null == url || "".equals(url.trim())) {
            Toast.makeText(context, "请输入正确地址", Toast.LENGTH_SHORT).show();
            return;
        }
        idWebWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings settings = idWebWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //不使用缓存：
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        idWebWebView.setWebChromeClient(new WebChromeClient());
        idWebWebView.setWebViewClient(new WebViewClient() {
            boolean isSuccess;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            /**
             * 加载成功
             *
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                Log.e("ceshi-:", "ceshi" + url);
                isLoading = false;

                if (null != idWebLoadLayout && isSuccess) {
//                    WeLog.d("!!---------onPageFinished");
                    idWebLoadLayout.setLoadSuccess(idWebWebView);
                    loadSuccess();
                }
//                idWebLoadLayout.setLoadStop(true, null, null);
            }

            /**
             * 开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isLoading = true;
//                idWebLoadLayout.setLoadStart();
                isSuccess = true;
                startLoading();
                if (null != idWebLoadLayout)
                    idWebLoadLayout.setLoadStart();
            }

            /**
             * 加载失败
             */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                isLoading = false;
                super.onReceivedError(view, errorCode, description, failingUrl);
//                idWebLoadLayout.setLoadStop(false, null, "访问失败!");
                isSuccess = false;
                if (null != idWebLoadLayout)
                    idWebLoadLayout.setLoadFailure("访问失败!");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isSuccess = false;
                loadFailure();
                if (null != idWebLoadLayout)
                    idWebLoadLayout.setLoadFailure("访问失败!");
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) { // 重写此方法可以让webview处理https请求
                // handler.proceed();
                super.onReceivedSslError(view, handler, error);
                isSuccess = false;
                loadFailure();
                if (null != idWebLoadLayout)
                    idWebLoadLayout.setLoadFailure("访问失败!");
            }
        });

        idWebLoadLayout.setBtnRetry(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idWebWebView.loadUrl(url);
            }
        });

        idWebWebView.loadUrl(url);
    }

    public void startLoading() {
    }

    public void loadSuccess() {
    }

    public void loadFailure() {
    }

}
