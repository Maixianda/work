package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMyDataBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.MyDataAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by zzwdream on 1/21/16.
 * 文档资料
 */
public class MyDataActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private ActivityMyDataBinding binding;
    private MyDataAdapter adapter;
    private int page = 1;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_data);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new MyDataAdapter(this);
        binding.rvMyData.setAdapter(adapter);
        binding.rvMyData.setLayoutManager(new LinearLayoutManager(this));
        adapter.onFinishLoadMore(true);
        binding.srvRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(onClick);
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
     * onClick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            LoadingHelper.showMaterLoading(v.getContext(), "加载中");
            onRefresh();
        }
    };

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("文档资料");
        header.setRightIcon(R.drawable.ic_search);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onMenuClicked() {
        IntentUtil.intentSearchActivity(MyDataActivity.this, Constants.SEARCH_TYPE[1], new PlaceAnOrder(), null);
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
     * 获取订单列表
     */
    private void getOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        userTypeSwitchOrder(map);
    }

    /**
     * 根据用户的type调用不同API
     *
     * @param map
     */
    private void userTypeSwitchOrder(Map<String, String> map) {
        Call<ListDTO<OrderInfo>> call = ApiUtil.getOrderService().getDocumentList(map);
        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleData(orderInfoListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(MyDataActivity.this, message);
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
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_document_data, false);
        page++;
    }
}
