package com.gzliangce.ui.activity.product;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gzliangce.AppContext;
import com.gzliangce.AppEnv;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityProductDetailsBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.ProductsDetailsDTO;
import com.gzliangce.dto.SupplementDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.ProductType;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.ProductInfoEvent;
import com.gzliangce.event.RefreshMyCollecttionProductEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.calculator.MortgageCalculatorActivity;
import com.gzliangce.ui.activity.order.PlaceAnOrderActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.dialog.ShareAppDialog;
import com.gzliangce.ui.widget.WebChromeClient;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.ShareUtil;
import com.squareup.otto.Subscribe;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import retrofit.Call;


/**
 * Created by leo on 16/1/21.
 * 产品详情界面（中介端跟客户端共用）
 */
public class ProductsDetailsActivity extends BaseSwipeBackActivityBinding implements WebChromeClient.OnProgressChangedListener, ShareAppDialog.OnShareAppListener {
    private ActivityProductDetailsBinding binding;
    private ProductsInfo productsInfo;
    private ShareAppDialog shareAppDialog;
    private long productId;
    private float viewSlop;
    private boolean isUpSlide;
    private boolean isBottomHide = false;
    private boolean isCollection = false;
    private boolean isBottomAnimStart;
    private boolean isPlaceOrederActivity = false;//是否是下单界面跳转进入
    private ObjectAnimator showOrderAnim, hideOrderAnim, showCounterAnim, hideCounterAnim;
    private ObjectAnimator amplificationAnim;
    private String content = "高效、专业、一站式优质服务，轻松实现你的梦想！";
    private AnimationDrawable animationDrawable;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
    }


    @Override
    public void initView() {
        binding.prProgress.setProgressDrawable(getResources().getDrawable(R.drawable.selector_progress_bg));
        amplificationAnim = AnimUtil.getAmplificationAnim(binding.ibtnCollections);
        binding.ivLoading.setImageResource(R.drawable.frame_loading);
        animationDrawable = (AnimationDrawable) binding.ivLoading.getDrawable();
        binding.ivLoading.setVisibility(View.GONE);
        LiangCeUtil.initWebView(binding.wbView);
        setBtnVisibility();
    }

    @Override
    public void initListener() {
        binding.ibtnBlack.setOnClickListener(onSingleClickListener);
        binding.wbView.setWebViewClient(webViewClient);
        binding.wbView.setWebChromeClient(new WebChromeClient(this));
        binding.ibtnBlack.setOnClickListener(onSingleClickListener);
        binding.ibtnCollections.setOnClickListener(onSingleClickListener);
        binding.ibtnShare.setOnClickListener(onSingleClickListener);
        binding.tvCounter.setOnClickListener(onSingleClickListener);
        binding.tvOrder.setOnClickListener(onSingleClickListener);
        binding.wbView.setOnTouchListener(onTouchListener);
    }


    @Override
    public void initData() {
        getSwipeBackLayout().setEdgeSize(100);
        viewSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        productsInfo = getIntent().getParcelableExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCollectionVisibilityAnim();
                getProductsDetails();
            }
        }, 100);
    }


    /**
     * 设置收藏按钮是否显示
     */
    private void setCollectionVisibilityAnim() {
        logger.e("ProductType()" + productsInfo.getProductType());
        if (Strings.isEquals(productsInfo.getProductType(), ProductType.BigType.toString())) {
            binding.ibtnCollections.setVisibility(View.GONE);
        } else {
            binding.ibtnCollections.setVisibility(View.VISIBLE);
        }
        getSupplementData();
    }


    /**
     * 根据登录用户类型设置按钮是否显示
     */
    private void setBtnVisibility() {
        isPlaceOrederActivity = getIntent().getBooleanExtra(Constants.REQUEST_ACTIVITY_PLACE_ORDER, false);
        if (!AppContext.me().isLogined()) {
            if (isPlaceOrederActivity) {
                binding.llyOrder.setVisibility(View.GONE);
            }
            return;
        }
        if (!Strings.isEquals(AppContext.me().getUser().getType(), UserType.mediator.toString())) {
            binding.llyOrder.setVisibility(View.GONE);
        }
        if (isPlaceOrederActivity) {
            binding.tvCounter.setVisibility(View.GONE);
            binding.llyOrder.setVisibility(View.GONE);
        }
    }


    /**
     * 避免打开网页时调用系统浏览器
     */
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!Strings.isEmpty(url) && !url.startsWith(AppEnv.BASE_URL)) {
                String id = url.substring(url.length() - 1);
                logger.e("id:" + id);
                ProductsInfo info = new ProductsInfo();
                info.setProductType(ProductType.smallType.toString());
                info.setId(Integer.parseInt(id));
                IntentUtil.actionProductsActivity(ProductsDetailsActivity.this, info);
                return true;
            }
            // 处理自定义scheme
            if (!url.startsWith("http")) {
                openSchemeUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            logger.e("view" + view.getTitle());
            super.onPageFinished(view, url);
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

    /**
     * 显示分享Dialog
     */
    private void showShareDialog() {
        if (shareAppDialog == null) {
            shareAppDialog = new ShareAppDialog(this, this);
        }
        shareAppDialog.show();
    }

    /**
     * onClick
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_black:
                    onBackPressed();
                    break;
                case R.id.ibtn_share:
                    showShareDialog();
                    break;
                case R.id.ibtn_collections:
                    isCollection();
                    break;
                case R.id.tv_counter:
                    startActivity(new Intent(ProductsDetailsActivity.this, MortgageCalculatorActivity.class));
                    break;
                case R.id.tv_order:
                    actionJoinOrder();
                    break;
            }
        }
    };

    /**
     * 跳转到我的订单界面
     */
    private void actionJoinOrder() {
        if (AppContext.me().isLogined() && !AppContext.me().isAuthorization()) {
            DialogUtil.AtterstationDialog(this, "未通过资格认证前无法下单");
            return;
        }
        Intent intent = new Intent(ProductsDetailsActivity.this, PlaceAnOrderActivity.class);
        intent.putExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY, productsInfo);
        startActivity(intent);
    }


    /**
     * OnTouchListener 事件
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        float lastY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float disY = event.getY() - lastY;
                    //垂直方向滑动
                    calculateMobileistance(disY);
                    break;
            }
            return false;
        }
    };


    /**
     * 计算滑动距离
     */
    private void calculateMobileistance(float disY) {
        if (Math.abs(disY) > viewSlop) {
            //是否向上滑动
            isUpSlide = disY < 0;
            //实现底部tools的显示与隐藏
            if (isUpSlide && !isBottomHide) {
                hideOrderBottom(binding.llyOrder);
                hideCounterBottom(binding.flyCounter);
            } else if (!isUpSlide && isBottomHide) {
                showOrderBottom(binding.llyOrder);
                showCounterAnimBottom(binding.flyCounter);
            }
        }
    }


    /**
     * 显示下单按钮
     */
    public void showOrderBottom(View view) {
        if (isBottomAnimStart) {
            return;
        }
        if (showOrderAnim == null) {
            showOrderAnim = AnimUtil.getYSubtractAnim(view);
        }
        isBottomHide = false;
        showOrderAnim.start();
    }


    /**
     * 隐藏下单按钮
     */
    public void hideOrderBottom(View view) {
        if (isBottomAnimStart) {
            return;
        }
        if (hideOrderAnim == null) {
            hideOrderAnim = AnimUtil.getYAppendAnim(view, null);
        }
        isBottomHide = true;
        hideOrderAnim.start();
    }

    /**
     * 隐藏计算器按钮
     */
    public void hideCounterBottom(View view) {
        if (hideCounterAnim == null) {
            hideCounterAnim = AnimUtil.getXAppendAnim(view, bottomHideListener);
        }
        isBottomHide = true;
        hideCounterAnim.start();
    }


    /**
     * 显示计算器按钮
     */
    public void showCounterAnimBottom(View view) {
        if (isBottomAnimStart) {
            return;
        }
        if (showCounterAnim == null) {
            showCounterAnim = AnimUtil.getXSubtractAnim(view, bottomHideListener);
        }
        isBottomHide = false;
        showCounterAnim.start();

    }


    /**
     * 动画播放监听
     */
    private Animator.AnimatorListener bottomHideListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isBottomAnimStart = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isBottomAnimStart = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    /**
     * 刷新我收藏的产品列表
     */
    private void postProductEvent() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventHub.post(new RefreshMyCollecttionProductEvent());
            }
        });
    }


    /**
     * 接收登录Event,判断是否关闭页面，mortgage类型用户是不能打开此页面，所以需要关闭
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        AccountInfo info = AppContext.me().getUser();
        if (Strings.isEquals(info.getType(), UserType.mortgage.toString())) {
            finish();
        } else {
            setBtnVisibility();
            getSupplementData();
        }
    }


    /**
     * 判断是否收藏
     */
    private void isCollection() {
        if (!AppContext.me().isLogined()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (!isCollection) {
            setCollectionState(true);
            amplificationAnim.start();
            postCollection(productsInfo.getId() + "");
        } else {
            setCollectionState(false);
            postDeleteCollection(productsInfo.getId() + "");
        }
    }


    /**
     * 设置按钮状态
     */
    private void setCollectionState(boolean collecction) {
        if (collecction) {
            isCollection = true;
            binding.ibtnCollections.setImageResource(R.drawable.ic_collection_true);
        } else {
            isCollection = false;
            binding.ibtnCollections.setImageResource(R.drawable.ic_collection_false);
        }
    }


    /**
     * 收藏
     */
    private void postCollection(String productId) {
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postProduct("product", productId);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(ProductsDetailsActivity.this, "已收藏");
                postProductEvent();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ProductsDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 取消收藏
     */
    private void postDeleteCollection(String productId) {
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postDeleteProduct(productId + "");
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(ProductsDetailsActivity.this, "已取消收藏");
                postProductEvent();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ProductsDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 获取产品额外数据
     */
    private void getSupplementData() {
        if (binding.ibtnCollections.getVisibility() == View.GONE) {
            return;
        }
        if (!AppContext.me().isLogined()) {
            binding.ibtnCollections.setVisibility(View.VISIBLE);
            return;
        }
        binding.ivLoading.setVisibility(View.VISIBLE);
        animationDrawable.start();
        binding.ibtnCollections.setVisibility(View.INVISIBLE);
        if (productsInfo == null) {
            binding.ivLoading.setVisibility(View.GONE);
            animationDrawable.stop();
            binding.ibtnCollections.setVisibility(View.VISIBLE);
            return;
        }
        Call<SupplementDTO> call = ApiUtil.getOtherDataService().getSupplementData(productsInfo.getId());
        call.enqueue(new APICallback<SupplementDTO>() {
            @Override
            public void onSuccess(SupplementDTO supplementDTO) {
                handlerSupplementData(supplementDTO);
            }

            @Override
            public void onFailed(String message) {
            }

            @Override
            public void onFinish() {
                binding.ivLoading.setVisibility(View.GONE);
                animationDrawable.stop();
                binding.ibtnCollections.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 设置产品收藏状态
     */
    private void handlerSupplementData(SupplementDTO supplementDTO) {
        if (supplementDTO == null) {
            return;
        }
        if (supplementDTO.getSupplement().getIsFavorite() == 0) {
            setCollectionState(false);
        } else {
            setCollectionState(true);
        }
    }

    /**
     * 获取产品详情
     */
    private void getProductsDetails() {
        if (productsInfo == null) {
            return;
        }
        productId = productsInfo.getId();
        LoadingHelper.showMaterLoading(this, "加载中");
        Call<ProductsDetailsDTO> call = ApiUtil.getOrderService().getProductdetail(productId);
        call.enqueue(new APICallback<ProductsDetailsDTO>() {
            @Override
            public void onSuccess(ProductsDetailsDTO productsDetailsDTO) {
                handlerProductsDetails(productsDetailsDTO);
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }


    /**
     * 处理产品详情数据
     */
    private void handlerProductsDetails(ProductsDetailsDTO detailsDTO) {
        if (detailsDTO == null) {
            ToastHelper.showMessage(this, "获取服务器数据失败");
            return;
        }
        detailsDTO.getProduct().setProductType(productsInfo.getProductType());
        productsInfo = detailsDTO.getProduct();
        if (productsInfo != null) {
            binding.tvTitle.setText(productsInfo.getProductName());
            binding.wbView.loadUrl(productsInfo.getUrl());
            binding.tvTitle.setText(productsInfo.getProductName());
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 分享接口回调
     */
    @Override
    public void onShareSina() {
        ShareUtil.shareApp(this, SinaWeibo.NAME, "良策金服" + productsInfo.getProductName(), content, productsInfo.getUrl(), null);
    }

    @Override
    public void onShareWechat() {
        ShareUtil.shareApp(this, Wechat.NAME, "良策金服" + productsInfo.getProductName(), content, productsInfo.getUrl(), null);
    }

    @Override
    public void onShareWechatMoments() {
        ShareUtil.momentShare(this, content, productsInfo.getProductName());
    }

    @Override
    public void onShareQQfriend() {
        ShareUtil.shareApp(this, QQ.NAME, "良策金服" + productsInfo.getProductName(), content, productsInfo.getUrl(), null);
    }

    @Override
    public void onShareQQZone() {
        ShareUtil.shareApp(this, QZone.NAME, "良策金服" + productsInfo.getProductName(), content, productsInfo.getUrl(), null);
    }


}
