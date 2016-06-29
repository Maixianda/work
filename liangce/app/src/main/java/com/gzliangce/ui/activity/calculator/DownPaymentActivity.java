package com.gzliangce.ui.activity.calculator;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityDownPaymentBinding;
import com.gzliangce.entity.DownPaymentInfo;
import com.gzliangce.ui.adapter.DownPaymentAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.MathUtils;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;

/**
 * Created by leo on 16/2/2.
 * 首付比例
 */
public class DownPaymentActivity extends BaseSwipeBackActivityBinding {
    private ActivityDownPaymentBinding binding;
    private DownPaymentAdapter paymentAdapter;
    private String[] scaleName = {"一成", "二成", "三成", "四成", "五成", "六成", "七成", "八成", "九成", "自定义"};
    private int houseTotal;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_down_payment);
        setHeader();
    }

    @Override
    public void initView() {
        paymentAdapter = new DownPaymentAdapter(this);
        binding.rvDownPayment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDownPayment.setAdapter(paymentAdapter);
    }

    /**
     * 获取房款总价数据
     */
    private void getIntentData() {
        houseTotal = getIntent().getIntExtra(Constants.REQUEST_ACTIVITY_HOUSER_PRICE, 0);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        paymentAdapter.getBinding().etCustomInterest.setOnEditorActionListener(editorActionListener);
    }

    @Override
    public void initData() {
        getIntentData();
        addData();
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 添加首付比例数据
     */
    private void addData() {
        for (int i = 0; i < 10; i++) {
            if (i == 2) {
                paymentAdapter.add(new DownPaymentInfo(scaleName[i], true, i + 1));
            } else {
                paymentAdapter.add(new DownPaymentInfo(scaleName[i], false, i + 1));
            }
        }
        paymentAdapter.notifyDataSetChanged();
    }


    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("首付比例");
        header.setLeftIcon(R.drawable.ic_back);
        binding.setHeader(header);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            actionScale();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 键盘完成按钮监听
     */
    private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionScale();
                return true;
            }
            return false;
        }
    };


    /**
     * 计算比例
     */
    private void actionScale() {
        Systems.hideKeyboard(this);
        String etPrices = paymentAdapter.getBinding().etCustomInterest.getText().toString().trim();
        if (Strings.isEmpty(etPrices)) {
            return;
        }
        float Price = Integer.parseInt(etPrices);
        if (Price > houseTotal) {
            ToastHelper.showMessage(this, "首付金额不能大于房款总额");
            return;
        }
        double scale = MathUtils.div(Price, houseTotal, 2);
        Intent intent = new Intent(this, MortgageCalculatorActivity.class);
        intent.putExtra(Constants.REQUEST_ACTIVITY_HOUSER_SCALE, scale);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
