package com.gzliangce.ui.activity.attestation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityForgotPasswordBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.TimeCount;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Regs;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * 忘记密码
 * Created by leo on 16/1/6.
 */
public class ForgotPasswordActivity extends BaseSwipeBackActivityBinding implements TextWatcher, TimeCount.TimeCountListener {
    private ActivityForgotPasswordBinding binding;
    private TimeCount timeCount;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        setHeader();
    }

    @Override
    public void initView() {
        timeCount = new TimeCount(Constants.ILLIS_IN_FUTURE, Constants.COUNT_DOWN_INTERVAL, this);
    }

    @Override
    public void initListener() {
        binding.tvNext.setOnClickListener(onSingleClickListener);
        binding.tvGetCode.setOnClickListener(onSingleClickListener);
        binding.etPhoneNumber.addTextChangedListener(this);
        binding.etVerificationCode.addTextChangedListener(this);
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        header.setMidTitle("忘记密码");
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


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_next:
                    actionResetActivity();
                    break;
                case R.id.tv_get_code:
                    actionGetSms();
                    break;
            }
        }
    };


    /**
     * 判断是否满足获取验证码条件
     */
    private void actionGetSms() {
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        if (Strings.isEmpty(phoneNumber)) {
            ToastHelper.showMessage(this, "请先输入手机号码");
            return;
        }
        if (!Regs.isPhone(phoneNumber)) {
            ToastHelper.showMessage(this, "手机号码格式不合法");
            return;
        }
        binding.tvGetCode.setSelected(true);
        binding.tvGetCode.setEnabled(false);
        timeCount.start();
        getForgotSms(phoneNumber);

    }


    /**
     * 获取验证码请求
     */
    private void getForgotSms(String phoneNumber) {
        Call<BaseDTO> call = ApiUtil.getAttestation().getForgotSmsCode(phoneNumber);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {

            }

            @Override
            public void onFailed(String message) {
                timeCount.cancel();
                timeCount.onFinish();
                ToastHelper.showMessage(ForgotPasswordActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 跳转到重置密码界面
     */
    private void actionResetActivity() {
        final String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        final String code = binding.etVerificationCode.getText().toString().trim();
        LoadingHelper.showMaterLoading(this, "请稍候");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postValidateSms(phoneNumber, code);
            }
        }, 1000);
    }

    /**
     * 判断验证码是否正确
     */
    private void postValidateSms(final String phone, final String code) {
        ApiUtil.postValidateSms(phone, code, new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                actionResetPassword(phone, code);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ForgotPasswordActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 跳转到重置密码界面
     */
    private void actionResetPassword(String phone, String code) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(Constants.USER_PHONE_NUMBER, phone);
        intent.putExtra(Constants.USER_SMS_CODE, code);
        startActivity(intent);
    }


    /**
     * 验证码倒计时进度回调
     */
    @Override
    public void onTick(long millisUntilFinished) {
        binding.tvGetCode.setText(millisUntilFinished / 1000 + "s");
    }


    /**
     * 验证码倒计时结束
     */
    @Override
    public void onFinish() {
        binding.tvGetCode.setSelected(false);
        binding.tvGetCode.setEnabled(true);
        binding.tvGetCode.setText("获取验证码");
    }


    /**
     * 输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置按钮是否可以点击
     */
    private void setBtnState() {
        binding.tvNext.setEnabled(false);
        binding.tvNext.setSelected(false);
        if (Strings.isEmpty(binding.etPhoneNumber.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etVerificationCode.getText().toString())) {
            return;
        }
        binding.tvNext.setEnabled(true);
        binding.tvNext.setSelected(true);
    }
}
