package com.gzliangce.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityAboutWebBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.widget.WebChromeClient;
import com.gzliangce.util.LiangCeUtil;

import java.lang.reflect.InvocationTargetException;

import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/1/13.
 * 关于我们子界面 - 复用
 */
public class WebActivity extends BaseSwipeBackActivityBinding implements WebChromeClient.OnProgressChangedListener {
    private ActivityAboutWebBinding binding;
    private String url, title;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_web);
        setHeader();
    }

    @Override
    public void initView() {
        binding.prProgress.setProgressDrawable(getResources().getDrawable(R.drawable.selector_progress_bg));
        LiangCeUtil.initWebView(binding.wbView);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.wbView.setWebViewClient(webViewClient);
        binding.wbView.setWebChromeClient(new WebChromeClient(this));
    }

    @Override
    public void initData() {
        getSwipeBackLayout().setEdgeSize(100);
        url = getIntent().getStringExtra(Constants.ABOUT_TEXT_URL);
        title = getIntent().getStringExtra(Constants.ABOUT_TEXT_TITLE);
        if (Strings.isNotEmpty(title)) {
            header.setMidTitle(title);
        }
        if (Strings.isNotEmpty(url)) {
            logger.e("url" + url);
            binding.wbView.loadUrl(url);
        }
    }



    /**
     * 避免打开网页时调用系统浏览器
     */
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 处理自定义scheme
            if (!url.startsWith("http")) {
                openSchemeUrl(url);
                return true;
            }
            return false;
        }


    };


    /**
     * 打开不是http开头的链接
     */
    private void openSchemeUrl(String url) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    /**
     * 网页监听加载进度
     */
    @Override
    public void onProgress(int newProgress) {
        if (binding.prProgress.getVisibility() == View.GONE) {
            binding.prProgress.setVisibility(View.VISIBLE);
        }
        binding.prProgress.setProgress(newProgress);
    }

    /**
     * 网页加载完成
     */
    @Override
    public void onProgressFinish(String title) {
        binding.prProgress.setProgress(100);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.prProgress.setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        try {
            binding.wbView.getClass().getMethod("onPause").invoke(binding.wbView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            binding.wbView.getClass().getMethod("onResume").invoke(binding.wbView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}
