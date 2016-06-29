package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityAddOrderBinding;
import com.gzliangce.dto.AddOrderDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import retrofit.Call;

/**
 * Created by leo on 16/3/1.
 * 添加订单界面
 */
public class AddOrderActivity extends BaseSwipeBackActivityBinding implements TextWatcher {
    private ActivityAddOrderBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_order);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.etCode.addTextChangedListener(this);
        binding.etOrderNumber.addTextChangedListener(this);
        binding.tvAdd.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
    }

    /**
     * 判断是否满足添加订单条件
     */
    private void isAddOrder() {
        binding.tvAdd.setSelected(false);
        binding.tvAdd.setEnabled(false);
        String number = binding.etOrderNumber.getText().toString().trim();
        String code = binding.etCode.getText().toString().trim();
        if (Strings.isEmpty(number)) {
            return;
        }
        if (Strings.isEmpty(code)) {
            return;
        }
        binding.tvAdd.setSelected(true);
        binding.tvAdd.setEnabled(true);
    }


    /**
     * 关联订单
     */
    private void addOrder() {
        final String number = binding.etOrderNumber.getText().toString().trim();
        String code = binding.etCode.getText().toString().trim();
        LoadingHelper.showMaterLoading(this, "正在添加订单");
        Call<AddOrderDTO> call = ApiUtil.getOrderService().simpleUserAddOrder(number, code);
        call.enqueue(new APICallback<AddOrderDTO>() {
            @Override
            public void onSuccess(AddOrderDTO addOrderDTO) {
                ToastHelper.showMessage(AddOrderActivity.this, "添加成功");
                handleData(number);
            }

            @Override
            public void onFailed(String message) {
                showErrorDialog(message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 处理关联订单数据
     */
    private void handleData(String number) {
        OrderInfo info = new OrderInfo();
        info.setNumber(number);
        Intent intent = new Intent(this, QureyOrderResultsActivity.class);
        intent.putExtra(Constants.ORDER_INFO, info);
        startActivity(intent);
        finish();
    }

    /**
     * 关联订单失败提示dialog
     */
    private void showErrorDialog(String message) {
        if (Strings.isEmpty(message)) {
            return;
        }
        DialogUtil.showHintDialog(this, message);
    }


    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("添加订单");
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 点击事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            addOrder();
        }
    };

    /**
     * 输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        isAddOrder();
    }
}
