package com.gzliangce.ui.activity.order;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityQureyOrderResultsBinding;
import com.gzliangce.dto.OrderDetailDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.SimpleUserOrderAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import retrofit.Call;

/**
 * Created by leo on 16/3/1.
 * 查询结果
 */
public class QureyOrderResultsActivity extends BaseSwipeBackActivityBinding {
    private ActivityQureyOrderResultsBinding binding;
    private OrderInfo orderInfo;
    private SimpleUserOrderAdapter adapter;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qurey_order_results);
        setHeader();
    }

    @Override
    public void initView() {
        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constants.ORDER_INFO);
        adapter = new SimpleUserOrderAdapter(this);
        adapter.onFinishLoadMore(true);
        binding.rvQureyOrder.setLayoutManager(new LinearLayoutManager(this));
        binding.rvQureyOrder.setAdapter(adapter);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getOrderDetail(orderInfo);
    }


    /**
     * 获取订单详情
     */
    private void getOrderDetail(OrderInfo info) {
        if (info == null) {
            return;
        }
        LoadingHelper.showMaterLoading(this, "加载中");
        Call<OrderDetailDTO> call = ApiUtil.getOrderService().getOrderDetail(info.getNumber());
        call.enqueue(new APICallback<OrderDetailDTO>() {
            @Override
            public void onSuccess(OrderDetailDTO orderDetailDTO) {
                handlerData(orderDetailDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(QureyOrderResultsActivity.this, message + "");
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }


    /**
     * 处理订单详情数据
     */
    private void handlerData(OrderDetailDTO orderDetailDTO) {
        if (orderDetailDTO == null || orderDetailDTO.getOrderDetail() == null) {
            return;
        }
        adapter.add(orderDetailDTO.getOrderDetail());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("查询结果");
        header.onBackClickListener();
        binding.setHeader(header);
    }
}
