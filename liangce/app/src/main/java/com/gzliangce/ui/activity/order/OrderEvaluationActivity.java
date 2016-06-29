package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityOrderEvaluationBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.OrderDetailDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.util.Strings;
import retrofit.Call;

/**
 * Created by aaron on 2/14/16.
 * 订单评价
 */
public class OrderEvaluationActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener, TextWatcher {
    private ActivityOrderEvaluationBinding binding;
    private String orderNumber;
    private String temp;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_evaluation);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.tvCommitEvaluation.setOnClickListener(this);
        binding.etComment.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit_evaluation:
                postOrderEvaluation();
                break;
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("评价订单");
        header.setRightBackground(0);
        header.setRightClickable(false);
        binding.setHeader(header);
    }

    /**
     * 评价订单
     */
    private void postOrderEvaluation() {
        logger.e("orderNumber:" + orderNumber);
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", orderNumber);
        map.put("serviceAttitude", binding.rbServiceAttitude.getRating() + "");
        map.put("professionalAbility", binding.rbProfessionalAbility.getRating() + "");
        map.put("speed", binding.rbSpeed.getRating() + "");
        map.put("comment", binding.etComment.getText() + "");
        LoadingHelper.showMaterLoading(this, "提交中...");
        Call<BaseDTO> call = ApiUtil.getOrderService().postEvaluationOrder(map);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                ToastHelper.showMessage(OrderEvaluationActivity.this, "提交评价成功");
                Intent intent = new Intent(OrderEvaluationActivity.this, OrderDetailsActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(OrderEvaluationActivity.this, message);
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFinish() {
            }
        });
    }


    /**
     * 输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (Strings.isEmpty(temp)) {
            binding.etComment.setText("70");
            return;
        }
        String limitSubstring = LiangCeUtil.getLimitSubstring(temp, 70, binding.tvHint);
        if (!Strings.isEmpty(limitSubstring)) {
            if (!limitSubstring.equals(temp)) {
                binding.etComment.setText(limitSubstring);
                binding.etComment.setSelection(limitSubstring.length());
            }
        }
    }
}
