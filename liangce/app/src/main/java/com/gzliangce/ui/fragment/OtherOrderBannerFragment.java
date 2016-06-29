package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentOtherOrderBannerBinding;
import com.gzliangce.dto.BannerDTO;
import com.gzliangce.dto.HomeProDuctDTO;
import com.gzliangce.entity.BannerImageInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.MenuType;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.ProductsAdapter;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.ui.custom.NetworkImageHolderView;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MetadataUtil;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/2/24.
 * 其他带广告轮播 - fragment
 */
public class OtherOrderBannerFragment extends BaseFragmentBinding implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    private Logger logger = LoggerFactory.getLogger(OtherOrderBannerFragment.class);
    private FragmentOtherOrderBannerBinding binding;
    private List<BannerImageInfo> bannerImageInfoList = new ArrayList<BannerImageInfo>();
    private ProductsAdapter productsAdapter;
    private NetworkImageHolderView networkImageHolderView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOtherOrderBannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_other_order_banner;
    }

    @Override
    public void initView() {
        binding.rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 4));
        productsAdapter = new ProductsAdapter(getActivity());
        binding.rvAllProducts.setAdapter(productsAdapter);
        initDirectOrderBtnState();
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.tvOrder.setOnClickListener(onClick);
        binding.hint.tvLoad.setOnClickListener(onClick);
        binding.srvLayout.setOnTouchListener(touchListener);
    }

    @Override
    public void initData() {
        binding.srvRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * OnTouchListener
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            binding.srvRefresh.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (binding.srvLayout.getScrollY() == 0) {
                    binding.srvRefresh.setEnabled(true);
                } else {
                    binding.srvRefresh.setEnabled(false);
                }
            }
            return false;
        }
    };

    /**
     * 初始化立即下单按钮
     */
    private void initDirectOrderBtnState() {
        if (LiangCeUtil.getUserType() == UserType.mediator) {
            binding.tvOrder.setVisibility(View.VISIBLE);
        } else {
            binding.tvOrder.setVisibility(View.GONE);
        }
    }

    /**
     * 立即下单 onClick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_order:
                    IntentUtil.actionDirectOrder(getContext());
                    break;
                case R.id.tv_load:
                    LoadingHelper.showMaterLoading(getContext(), "加载中");
                    onRefresh();
                    break;
            }
        }
    };


    /**
     * 获取广告数据
     */
    private void getBannerData() {
        String port = getPort();
        String index = Constants.INDEX;
        Call<BannerDTO> call = ApiUtil.getOtherDataService().getBannerData(port, index);
        call.enqueue(new APICallback<BannerDTO>() {
            @Override
            public void onSuccess(BannerDTO bannerDTO) {
                handlerBannerData(bannerDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
                isSetHint(true, R.string.http_on_failed);
                hideLoadding();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    /**
     * 初始化广告banner数据
     */
    private void initBannerData() {
        binding.cbBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return getNetworkView();
            }
        }, bannerImageInfoList);
        binding.cbBanner.setPageIndicator(new int[]{R.drawable.ic_white_dot, R.drawable.ic_red_dot}, getResources().getDimensionPixelSize(R.dimen.dp_7));
        binding.cbBanner.setOnItemClickListener(OtherOrderBannerFragment.this);
        if (bannerImageInfoList.size() >= 2) {
            binding.cbBanner.setPointViewVisible(true);
            binding.cbBanner.startTurning(5000);
            binding.cbBanner.setCanLoop(true);
        } else {
            binding.cbBanner.setPointViewVisible(false);
            binding.cbBanner.stopTurning();
            binding.cbBanner.setCanLoop(false);
        }
    }

    /**
     * 单例获取banner view
     */
    private NetworkImageHolderView getNetworkView() {
        if (networkImageHolderView == null) {
            networkImageHolderView = new NetworkImageHolderView(getActivity());
        }
        return networkImageHolderView;
    }

    /**
     * 获取产品数据
     */
    private void getHomeProducts() {
        Call<HomeProDuctDTO> call = ApiUtil.getOrderService().getHomeProducts();
        call.enqueue(new APICallback<HomeProDuctDTO>() {
            @Override
            public void onSuccess(HomeProDuctDTO homeProDuctDTO) {
                handlerProductData(homeProDuctDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
                isSetHint(true, R.string.http_on_failed);
            }

            @Override
            public void onFinish() {
                hideLoadding();
            }
        });
    }

    /**
     * 处理产品数据
     */
    private void handlerProductData(final HomeProDuctDTO homeProDuctDTO) {
        if (homeProDuctDTO == null) {
            return;
        }
        productsAdapter.clear();
        productsAdapter.addAll(homeProDuctDTO.getList());
        productsAdapter.add(new ProductsInfo("全部产品", R.drawable.ic_all_product, MenuType.allProduct));
        productsAdapter.add(new ProductsInfo("我的订单", R.drawable.ic_all_order, MenuType.myOrder));
        if (LiangCeUtil.getUserType() == UserType.mediator) {
            productsAdapter.add(new ProductsInfo("金融经纪人", R.drawable.ic_all_user, MenuType.BrokerList));
        }
        productsAdapter.notifyDataSetChanged();
        isSetHint(false, R.string.no_product);
    }


    /**
     * 隐藏Loading
     */
    private void hideLoadding() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(false);
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 是否显示加载失败提示
     */
    private void isSetHint(boolean isFailed, int strId) {
        int count = bannerImageInfoList.size() + productsAdapter.size();
        UiUtil.isSetFailedHint(count, binding.hint.tvLoad, binding.srvRefresh, strId, isFailed);
    }

    /**
     * 获取请求广告的用户类型
     */
    private String getPort() {
        String port = UserType.mediator.toString();
        if (AppContext.me().isLogined()) {
            port = LiangCeUtil.getUserType().toString();
        }
        return port;
    }


    /**
     * 处理广告数据
     */
    private void handlerBannerData(final BannerDTO bannerDTO) {
        if (bannerDTO != null) {
            if (bannerDTO.getList().size() > 0) {
                binding.cbBanner.setVisibility(View.VISIBLE);
                bannerImageInfoList.clear();
                bannerImageInfoList.addAll(bannerDTO.getList());
                initBannerData();
            } else {
                binding.cbBanner.setVisibility(View.GONE);
            }
        }
        //广告数据处理完之后再请求产品列表数据
        getHomeProducts();
    }


    @Override
    public void onPause() {
        binding.cbBanner.stopTurning();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (bannerImageInfoList.size() >= 2) {
            binding.cbBanner.startTurning(5000);
        }
        super.onResume();
    }


    @Override
    public void onRefresh() {
        binding.cbBanner.stopTurning();
        binding.srvRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBannerData();
            }
        }, 500);
    }


    /**
     * 广告点击事件
     */
    @Override
    public void onItemClick(int i) {
        BannerImageInfo imageInfo = bannerImageInfoList.get(i);
        if (Strings.isEmpty(imageInfo.getUrl())) {
            return;
        }
        IntentUtil.actionNewSDetaiActivity(getContext(), imageInfo.getTitle(), imageInfo.getUrl());
    }

    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (event != null) {
            addAllBrokerMenu();
            removeAllBrokerMenu();
            initData();
            initDirectOrderBtnState();
            actionPlaceOrder();
        }
    }

    /**
     * 接收退出账号Event,切换Fragment
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        if (event != null) {
            addAllBrokerMenu();
            initData();
            initDirectOrderBtnState();
        }
    }

    /**
     * 提交未登录时产生的订单
     */
    public void actionPlaceOrder() {
        if (!LiangCeUtil.judgeUserType(UserType.mediator)) {
            CacheManager.disk(getContext()).remove(Constants.SAVE_OREDER_INFO);//离线下单后，登录用户不是中介，订单数据作废
            return;
        }
        final PlaceAnOrder order = (PlaceAnOrder) MetadataUtil.getGCache(getContext(), Constants.SAVE_OREDER_INFO);
        if (order == null) {
            return;
        }
        if (!AppContext.me().isAuthorization()) {
            CacheManager.disk(getContext()).remove(Constants.SAVE_OREDER_INFO);//离线下单后，登录中介未通过认证，订单数据作废
            return;
        }
        ApiDataUtil.doPlaceAnOrder(order, getActivity());
    }


    /**
     * 普通用户和中介用户切换时，添加金融经纪人按钮
     */
    private void addAllBrokerMenu() {
        if (LiangCeUtil.getUserType() != UserType.mediator) {
            return;
        }
        if (productsAdapter.size() <= 0) {
            return;
        }
        if (productsAdapter.get(productsAdapter.size() - 1).getType() != MenuType.BrokerList) {
            productsAdapter.add(new ProductsInfo("金融经纪人", R.drawable.ic_all_user, MenuType.BrokerList));
            productsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 普通用户和中介用户切换时，删除金融经纪人按钮
     */
    private void removeAllBrokerMenu() {
        if (LiangCeUtil.getUserType() != UserType.simpleUser) {
            return;
        }
        if (productsAdapter.size() <= 0) {
            return;
        }
        int size = productsAdapter.size() - 1;
        if (productsAdapter.get(size).getType() == MenuType.BrokerList) {
            productsAdapter.remove(size);
            productsAdapter.notifyDataSetChanged();
        }
    }

}
