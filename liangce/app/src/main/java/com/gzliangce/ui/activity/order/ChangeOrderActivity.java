package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityChangeOrderBinding;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;

/**
 * Created by leo on 16/1/14.
 * 转单界面
 */
public class ChangeOrderActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener, TextWatcher {
    private ActivityChangeOrderBinding binding;
    private PlaceAnOrder placeAnOrder;
    private String temp;
    private String mClassName;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_order);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.etReason.addTextChangedListener(this);
        binding.tvConfirmSelect.setOnClickListener(this);
        binding.etReason.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constants.ORDER_INFO);
        mClassName = getIntent().getStringExtra(Constants.REQUEST_BUTTON_STATUS);
        if (orderInfo != null) {
            placeAnOrder = new PlaceAnOrder();
            placeAnOrder.setAreaId(orderInfo.getAreaId() + "");
            placeAnOrder.setNumber(orderInfo.getNumber());
            header.setMidTitle("转单");
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_select:
                actionSelectActivity();
                break;
        }
    }


    /**
     * 跳转到金融经纪界面（地图）
     */
    private void actionSelectActivity() {
        Systems.hideKeyboard(this);
        placeAnOrder.setReason(binding.etReason.getText() + "");
        Intent intent = new Intent(this, SelectBrokerMapActivity.class);
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, mClassName);
        startActivity(intent);
    }


    /**
     * Edit输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s.toString();
        setBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (Strings.isEmpty(temp)) {
            binding.tvNum.setText("40");
            return;
        }
        String limitSubstring = LiangCeUtil.getLimitSubstring(temp, 40, binding.tvNum);
        if (!Strings.isEmpty(limitSubstring)) {
            if (!limitSubstring.equals(temp)) {
                binding.etReason.setText(limitSubstring);
                binding.etReason.setSelection(limitSubstring.length());
            }
        }
    }


    /**
     * 设置按钮是否满足可点击条件
     */
    private void setBtnState() {
        boolean state = true;
        if (Strings.isEmpty(binding.etReason.getText().toString().trim())) {
            state = false;
        }
        binding.tvConfirmSelect.setEnabled(state);
        binding.tvConfirmSelect.setSelected(state);
    }

}
