package com.gzliangce.ui.fragment.usercenter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentMyOrderBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.enums.OrderStatusType;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.CancleOrderEvent;
import com.gzliangce.event.ChangeOrderEvent;
import com.gzliangce.event.RefreshOrderListEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.usercenter.MyOrderActivity;
import com.gzliangce.ui.adapter.MyOrderAdapter;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.ui.callback.onDeleteCallBack;
import com.gzliangce.ui.widget.WrapContentLinearLayoutManager;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;


/**
 * 我的订单界面
 * Created by aaron on 8/21/14.
 */
public class MyOrderFragment extends BaseFragmentBinding implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(MyOrderFragment.class);
    private FragmentMyOrderBinding binding;
    private MyOrderAdapter adapter;
    private int position;
    private int page = 1;
    private OrderStatusType orderStatus;
    private UserType userType;
    private Class mClass;


    public static MyOrderFragment getInstance(int i, String title, UserType userType, Class mClass) {
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.setUserType(userType);
        fragment.setPosition(i);
        fragment.setFragmentTitle(title);
        fragment.switchStatus();
        fragment.setmClass(mClass);
        return fragment;
    }

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_my_order;
    }

    @Override
    public void initView() {
        adapter = new MyOrderAdapter(getActivity(), position, mClass);
        adapter.onFinishLoadMore(true);
        adapter.setMyOrderFragment(this);
        binding.rvMyOrder.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.rvMyOrder.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(this);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getOrderList();
            }
        });
    }

    @Override
    public void initData() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        binding.srvRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(true);
                onRefresh();
            }
        });
    }


    public RecyclerView getRecyclerView() {
        return binding.rvMyOrder;
    }

    @Override
    public void onRefresh() {
        page = 1;
        adapter.onFinishLoadMore(true);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderList();
            }
        }, 500);
    }

    /**
     * 根据position来设置状态
     */
    private void switchStatus() {
        switch (position) {
            case 1:
                //未接单
                orderStatus = OrderStatusType.wait;
                break;
            case 2:
                //已接单
                orderStatus = OrderStatusType.receive;
                break;
            case 3:
                //已取消
                orderStatus = OrderStatusType.cancel;
                break;
            case 4:
                //已签约
                orderStatus = OrderStatusType.signed;
                break;
        }
    }

    /**
     * 获取订单列表
     */
    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        map.put(Constants.STATUS, orderStatus.toString());
        userTypeSwitchOrder(map);
    }

    /**
     * 根据用户的type调用不同API
     *
     * @param map
     */
    private void userTypeSwitchOrder(Map<String, String> map) {
        Call<ListDTO<OrderInfo>> call = null;

        if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            // 中介获取我的订单数据列表
            call = ApiUtil.getOrderService().getMediatorMyOrderList(map);
        } else if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            // 按揭获取我的订单数据列表
            call = ApiUtil.getOrderService().getMortgageMyOrderList(map);
        } else {
            return;
        }

        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleData(orderInfoListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(getActivity(), message + "");
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 网络请求完成数据处理
     *
     * @param orderInfoList
     */
    private void handleData(List<OrderInfo> orderInfoList) {
        if (orderInfoList == null) {
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, orderInfoList, page);
        adapter.addAll(orderInfoList);
        adapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_order, false);
        page++;
    }

    /**
     * 对外公开的空数据提示方法
     */
    public void setHint() {
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_order, false);
    }

    /**
     * 接单成功后删除已接单列表数据
     */
    @Subscribe
    public void onRemoveOrderDataEvent(RefreshOrderListEvent event) {
        if (LiangCeUtil.getUserType() != userType) {
            return;
        }
        //如果Class的名称和当前发送Event的名称相等，因为adapter中已经对数据处理，则不再遍历删除数据。
        if (Strings.isEquals(mClass.getName(), event.getmClass().getName())) {
            return;
        }
        if (orderStatus != OrderStatusType.wait) {
            return;
        }
        ApiDataUtil.taskRemoveData(event.getOrderNumber(), adapter.getData(), adapter, callBack);
    }


    /**
     * 转单event
     */
    @Subscribe
    public void onChangeOrderEvent(ChangeOrderEvent event) {
        if (LiangCeUtil.getUserType() != userType) {
            return;
        }
        //如果Class的名称和当前发送Event的名称相等，因为adapter中已经对数据处理，则不再遍历删除数据。
        if (Strings.isEquals(mClass.getName(), event.getmClass().getName())) {
            return;
        }
        if (orderStatus != OrderStatusType.wait) {
            return;
        }
        ApiDataUtil.removeOrderData(event.getPlaceAnOrder(), false, adapter, callBack);
    }


    /**
     * 订单被取消event
     */
    @Subscribe
    public void onCancelOrderEvent(CancleOrderEvent event) {
        if (LiangCeUtil.getUserType() != userType) {
            return;
        }
        //如果Class的名称和当前发送Event的名称相等，因为adapter中已经对数据处理，则不再遍历删除数据。
        if (Strings.isEquals(mClass.getName(), event.getmClass().getName())) {
            return;
        }
        if (position != 1) {
            return;
        }
        ApiDataUtil.taskRemoveData(event.getOrderInfo().getNumber(), adapter.getData(), adapter, callBack);
    }


    /**
     * 订单item被删除
     */
    private onDeleteCallBack callBack = new onDeleteCallBack() {
        @Override
        public void onFinish() {
            UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_order, false);
        }
    };

    @Override
    public void onClick(View v) {
        LoadingHelper.showMaterLoading(getContext(), "加载中");
        onRefresh();
    }
}