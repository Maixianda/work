package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentMortgageOrderBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.CancleOrderEvent;
import com.gzliangce.event.ChangeOrderEvent;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.RefreshOrderListEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.MortgageOrderAdapter;
import com.gzliangce.ui.callback.onDeleteCallBack;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/1/23.
 * 首页
 */
public class MortgageOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Logger logger = LoggerFactory.getLogger(MortgageOrderFragment.class);
    private FragmentMortgageOrderBinding binding;
    private MortgageOrderAdapter adapter;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMortgageOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public FragmentMortgageOrderBinding getBinding() {
        return binding;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_mortgage_order;
    }

    @Override
    public void initView() {
        adapter = new MortgageOrderAdapter(getActivity(), this);
        adapter.onFinishLoadMore(true);
        binding.rvMortgageOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMortgageOrder.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                page++;
                getMortgageOrderList();
            }
        });
    }

    @Override
    public void initData() {
        if (!AppContext.me().isLogined() || LiangCeUtil.getUserType() != UserType.mortgage) {
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

    @Override
    public void onRefresh() {
        page = 1;
        adapter.onFinishLoadMore(true);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMortgageOrderList();
            }
        }, 500);
    }

    /**
     * 根据用户的type调用不同API
     */
    private void getMortgageOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        map.put(Constants.STATUS, Constants.WAIT);
        Call<ListDTO<OrderInfo>> call = ApiUtil.getOrderService().getMortgageMyOrderList(map);
        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleData(orderInfoListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
                adapter.hideLoadMore();
            }
        });
    }


    /**
     * 网络请求完成数据处理
     *
     * @param orderInfoList
     */
    private void handleData(List<OrderInfo> orderInfoList) {
        if (Collections.isEmpty(orderInfoList)) {
            AdapterUtil.setHint(adapter, binding.llHint);
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, orderInfoList, page);
        adapter.addAll(orderInfoList);
        adapter.notifyDataSetChanged();
        page++;
    }


    /**
     * 接收广播刷新列表
     */
    @Subscribe
    public void onRefreshOrderListEvent(RefreshOrderListEvent event) {
        if (!AppContext.me().isLogined() || LiangCeUtil.getUserType() != UserType.mortgage) {
            return;
        }
        ApiDataUtil.taskRemoveData(event.getOrderNumber(), adapter.getData(), adapter, callBack);
    }


    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (event != null) {
            initData();
        }
    }


    /**
     * 转单event
     */
    @Subscribe
    public void onChangeOrderEvent(ChangeOrderEvent event) {
        if (!AppContext.me().isLogined() || LiangCeUtil.getUserType() != UserType.mortgage) {
            return;
        }
        ApiDataUtil.removeOrderData(event.getPlaceAnOrder(), false, adapter, callBack);
    }


    /**
     * 订单被取消event
     */
    @Subscribe
    public void onCancelOrderEvent(CancleOrderEvent event) {
        if (!AppContext.me().isLogined() || LiangCeUtil.getUserType() != UserType.mortgage) {
            return;
        }
        ApiDataUtil.removeOrderData(event.getOrderInfo(), true, adapter, callBack);
    }


    /**
     * 订单item被删除
     */
    private onDeleteCallBack callBack = new onDeleteCallBack() {
        @Override
        public void onFinish() {
            if (adapter.getItemCount() <= 1) {
                binding.llHint.setVisibility(View.VISIBLE);
            }
        }
    };


}
