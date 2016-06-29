package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySimpleUserOrderBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.order.AddOrderActivity;
import com.gzliangce.ui.adapter.SimpleUserOrderAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.UiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/2/29.
 * 普通用户订单界面
 */
public class SimpleUserOderActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private ActivitySimpleUserOrderBinding binding;
    private SimpleUserOrderAdapter adapter;
    private int page = 1;
    private String keyword = "";

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_user_order);
    }

    @Override
    public void initView() {
        binding.srvRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        adapter = new SimpleUserOrderAdapter(this);
        adapter.onFinishLoadMore(true);
        binding.rvSimpleOrder.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSimpleOrder.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(onSingleClickListener);
        binding.ibtnBack.setOnClickListener(onSingleClickListener);
        binding.tvTitle.setOnClickListener(onSingleClickListener);
        binding.ibtnAdd.setOnClickListener(onSingleClickListener);
        binding.ibtnSeach.setOnClickListener(onSingleClickListener);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getOrderList();
            }
        });
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
     * 点击事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_back:
                    finish();
                    break;
                case R.id.ibtn_seach:
                    IntentUtil.intentSearchActivity(SimpleUserOderActivity.this, Constants.SEARCH_TYPE[0], new PlaceAnOrder(), null);
                    break;
                case R.id.ibtn_add:
                    IntentUtil.actionActivity(SimpleUserOderActivity.this, AddOrderActivity.class);
                    break;
                case R.id.tv_load:
                    LoadingHelper.showMaterLoading(v.getContext(), "加载中");
                    onRefresh();
                    break;
            }
        }
    };


    /**
     * 获取订单列表
     */
    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        if (!Strings.isEmpty(keyword)) {
            map.put(Constants.KEYWORD, keyword);
        }
        simpleUserOrder(map);
    }

    /**
     * 根据用户的type调用不同API
     *
     * @param map
     */
    private void simpleUserOrder(Map<String, String> map) {
        Call<ListDTO<OrderInfo>> call = ApiUtil.getOrderService().getSimpleUserMyOrderList(map);
        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleData(orderInfoListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                        binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(SimpleUserOderActivity.this, message + "");
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
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                binding.srvRefresh, R.string.no_order_notes, false);
        page++;
    }


    @Override
    public void onRefresh() {
        adapter.onFinishLoadMore(true);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderList();
            }
        }, 500);
    }

}
