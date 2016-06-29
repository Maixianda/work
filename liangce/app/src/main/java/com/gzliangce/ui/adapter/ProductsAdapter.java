package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemConvenientBannerBinding;
import com.gzliangce.databinding.ItemProductBinding;
import com.gzliangce.entity.BannerImageInfo;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.MenuType;
import com.gzliangce.enums.ProductType;
import com.gzliangce.enums.UserType;
import com.gzliangce.lclibrary.base.BaseWebActivity;
import com.gzliangce.lclibrary.ui.MenuBtnWebActivity;
import com.gzliangce.seccond_ver.SearchHourse.JSUtils;
import com.gzliangce.seccond_ver.SearchHourse.activity_header_test;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.order.SelectBrokerListActivity;
import com.gzliangce.ui.activity.product.AllProductActivity;
import com.gzliangce.ui.activity.usercenter.MyOrderActivity;
import com.gzliangce.ui.activity.usercenter.SimpleUserOderActivity;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;

import java.util.HashMap;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.crypto.Rsas;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/20.
 * 产品分类列表adapter
 */
public class ProductsAdapter extends ListAdapter<ProductsInfo, ViewDataBinding> implements OnItemClickListener {
    private Activity activity;
    private Logger logger = LoggerFactory.getLogger(ProductsAdapter.class);
    private List<BannerImageInfo> bannerImageInfos;
    private ItemConvenientBannerBinding bannerBinding;

    public ProductsAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void setBannerImageInfos(List<BannerImageInfo> bannerImageInfos) {
        this.bannerImageInfos = bannerImageInfos;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ViewDataBinding> vh, int position) {
        if (vh.getItemViewType() == R.layout.item_convenient_banner) {
            bannerBinding = (ItemConvenientBannerBinding) vh.getBinding();
            initBannerData(bannerBinding);
        } else {
            ItemProductBinding binding = (ItemProductBinding) vh.getBinding();
            ProductsInfo info = get(position);
            if (info.getType() == null) {
                PhotoLoader.display(binding.ibtnProductIcon, info.getIcon(), activity.getResources().getDrawable(R.drawable.ic_product_loading));
            } else {
                binding.ibtnProductIcon.setImageResource(info.getIconRes());
            }
            binding.llyProduct.setOnClickListener(new onClick(vh.getAdapterPosition()));
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        if (position == 0 && bannerImageInfos != null) {
            return R.layout.item_convenient_banner;
        }
        return R.layout.item_product;
    }


    /**
     * 初始化广告banner数据
     */
    private void initBannerData(ItemConvenientBannerBinding binding) {
        if (bannerImageInfos == null) {
            return;
        }
        if (bannerImageInfos.size() < 1) {
            stopBanner();
            binding.cbBanner.setVisibility(View.GONE);
            return;
        } else {
            startBaner();
            binding.cbBanner.setVisibility(View.VISIBLE);
        }
        UiUtil.initBannerView(activity, binding.cbBanner, bannerImageInfos, this);
    }


    /**
     * 停止广告轮播
     */
    public void stopBanner() {
        if (bannerBinding == null || bannerBinding.cbBanner == null) {
            return;
        }
        bannerBinding.cbBanner.stopTurning();
    }


    /**
     * 启动广告轮播
     */
    public void startBaner() {
        if (bannerBinding == null || bannerBinding.cbBanner == null) {
            return;
        }
        if (bannerImageInfos == null || bannerImageInfos.size() < 2) {
            return;
        }
        bannerBinding.cbBanner.startTurning(5000);
    }


    /**
     * 广告点击事件
     */
    @Override
    public void onItemClick(int i) {
        BannerImageInfo imageInfo = bannerImageInfos.get(i);
        if (Strings.isEmpty(imageInfo.getUrl())) {
            return;
        }
        IntentUtil.actionNewSDetaiActivity(getContext(), imageInfo.getTitle(), imageInfo.getUrl());
    }


    /**
     * 点击事件监听
     */
    private class onClick extends OnSingleClickListener {
        private int position;

        public onClick(int position) {
            this.position = position;
        }

        @Override
        public void onSingleClick(View v) {
            ProductsInfo info = get(position);
            if (info.getType() == null) {
                //region    查册写死
                if (info.getProductName().equals("查册")) {
                    Intent intent2 = new Intent(activity, activity_header_test.class);
                    intent2.putExtra(BaseWebActivity.WEB_URL, "http://test.lcedai.com/app_public/v1/search/search.html");
                    String js1 = "javascript:(function(){\n" +
                            "document.getElementById(\"tokenId\").value =' " + "我爱!!罗明通" + "'" +
                            "})()";

                    HashMap<String, String> map = new HashMap<>();
                    String token = "";
                    String timestamp = System.currentTimeMillis() + "";
                    String deviceId = Systems.getDeviceId(AppContext.me());
                    if (AppContext.me().isLogined()) {
                        token = AppContext.me().getUser().getToken();
                        map.put("token", token);
                        map.put("deviceId", deviceId);
                        String js = JSUtils.getChangeElementJs(map);

                        intent2.putExtra(MenuBtnWebActivity.JAVASCRIPT_KEY, js);
                        activity.startActivity(intent2);
                    } else {
                        IntentUtil.actionActivity(activity, LoginActivity.class);
                    }
                }
                //endregion 查册写死
                //region 测试下我的查册页面
                //endregion 测试下我的查册页面
                else {
                    actionIsSelect(info);
                }
            } else {
                onMenuAction(info.getType());
            }
        }
    }

    /**
     * 根据下单方式进行页面跳转/或选择
     */
    private void actionIsSelect(ProductsInfo info) {
        info.setProductType(ProductType.smallType.toString());
        IntentUtil.actionProductsActivity(activity, info);
    }

    /**
     * 本地添加按钮菜单点击 - v1.2版本功能
     */
    public void onMenuAction(MenuType type) {
        switch (type) {
            case allProduct:
                activity.startActivity(new Intent(activity, AllProductActivity.class));
                break;
            case myOrder:
                actionMyorderActivity();
                break;
            case BrokerList:
                doSelectBrokerListActivity();
                break;
        }
    }


    /**
     * 全部金融经纪列表
     */
    private void doSelectBrokerListActivity() {
        if (UserType.mediator != LiangCeUtil.getUserType()) {
            ToastHelper.showMessage(getContext(), "你不是中介用户，不能查看全部金融经纪");
            return;
        }
        Intent intent = new Intent(getContext(), SelectBrokerListActivity.class);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, ButtonStatus.OrderDirectBtn);
        getContext().startActivity(intent);
    }


    /**
     * 跳转到我的订单界面
     */
    public void actionMyorderActivity() {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(activity, LoginActivity.class);
            return;
        }
        //普通用户类型
        if (LiangCeUtil.getUserType() == UserType.simpleUser) {
            activity.startActivity(new Intent(activity, SimpleUserOderActivity.class));
        } else {
            activity.startActivity(new Intent(activity, MyOrderActivity.class));
        }
    }

}
