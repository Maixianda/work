package com.gzliangce.seccond_ver.SearchHourse;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.gzliangce.R;
import com.gzliangce.databinding.ActivityHeaderTestBinding;
import com.gzliangce.lclibrary.views.LoadingLayout;
import com.gzliangce.lclibrary.views.VDHLayout;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

import io.ganguo.library.common.LoadingHelper;

/**
 * Created by maixianda on 16-6-22.
 */
public class activity_header_test extends BaseSwipeBackActivityBinding implements View.OnClickListener{
    private ActivityHeaderTestBinding binding;

    private boolean isLoading;

    private String url;

    public static final String WEB_URL = "url";

    WebView idWebWebView;
    LoadingLayout idWebLoadLayout;

    VDHLayout vdh;
    TextView menuBtn;

    String javascript;
    public final static String JAVASCRIPT_KEY="JAVASCRIPT_KEY";
    private boolean isShowFinishBtn = false;//是否显示悬浮拖动按钮,false 不显示
    private boolean isShowLoadingView = false;//
    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_header_test);
        setHeader();

        url = getIntent().getStringExtra(WEB_URL);
    }
    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);

        header.setRightBackground(0);
        header.setRightIcon(0);


        header.setLeftBackground(0);
        binding.setHeader(header);
    }

    @Override
    public void onBackPressed() {
        header.setLeftIcon(R.drawable.ic_back);
        if(idWebWebView.canGoBack())
        {
            idWebWebView.goBack();
        }
        else
        {
            finish();
        }

    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void initView() {
        idWebWebView = (WebView) findViewById(com.gzliangce.lclibrary.R.id.id_webview);
        idWebLoadLayout = (LoadingLayout) findViewById(com.gzliangce.lclibrary.R.id.id_loadview);
        vdh = (VDHLayout) findViewById(com.gzliangce.lclibrary.R.id.id_vdh);
        menuBtn = (TextView) findViewById(com.gzliangce.lclibrary.R.id.id_menu_btn);
        menuBtn.setOnClickListener(this);

        header.setMidTitle("查册");
        if (isShowFinishBtn == false)
        {
            menuBtn.setVisibility(View.GONE);
        }

        if (isShowLoadingView ==false)
        {
            idWebLoadLayout.setVisibility(View.GONE);
        }

        javascript = getIntent().getStringExtra(JAVASCRIPT_KEY);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (null == url || "".equals(url.trim())) {
            Toast.makeText(this, "请输入正确地址", Toast.LENGTH_SHORT).show();
            return;
        }
        idWebWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings settings = idWebWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //不使用缓存：
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        idWebWebView.setWebChromeClient(new android.webkit.WebChromeClient());

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
                //super.onPageFinished(view, url);
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
        vdh.setVisibility(View.GONE);
        LoadingHelper.showMaterLoading(this, "加载中");
        menuBtn.setVisibility(View.GONE);
    }

    public void loadSuccess() {
        vdh.setVisibility(View.VISIBLE);
        LoadingHelper.hideMaterLoading();
        if (isShowFinishBtn == true) {
            menuBtn.setVisibility(View.VISIBLE);
        }

        if(null!=javascript||!javascript.trim().isEmpty()){
            Log.e("ceshi-:","javascript:   "+javascript);
//            loadJS=true;
//            idWebWebView.loadUrl("javascript:(function(){\n" +
//                    "document.getElementById(\"tokenId\").value =' " + "测试2" + "'" +
//                    "})()");
            idWebWebView.loadUrl(javascript);
        }
    }

    public void loadFailure() {
        LoadingHelper.hideMaterLoading();
        vdh.setVisibility(View.GONE);
        menuBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
