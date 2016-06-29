package com.gzliangce.ui.activity.attestation;

import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityResetPasswordBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/1/6.
 * 重置密码
 */
public class ResetPasswordActivity extends BaseSwipeBackActivityBinding implements TextWatcher {
    private ActivityResetPasswordBinding binding;
    private String phone, smsCode;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.etConfirmPassword.addTextChangedListener(this);
        binding.etNewPassword.addTextChangedListener(this);
        binding.tvConfirmEdit.setOnClickListener(onSingleClickListener);
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra(Constants.USER_PHONE_NUMBER);
        smsCode = getIntent().getStringExtra(Constants.USER_SMS_CODE);
        header.setMidTitle("重置密码");
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
        binding.setHeader(header);
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_confirm_edit:
                    actionResetPassword();
                    break;
            }
        }
    };

    /**
     * 确认重置密码
     */
    private void actionResetPassword() {
        final String newPassword = binding.etNewPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        if (LiangCeUtil.passwdCheck(newPassword) || LiangCeUtil.passwdCheck(confirmPassword)) {
            ToastHelper.showMessage(this, "密码中不允许存在中文");
            return;
        }
        if (newPassword.length() < 6) {
            ToastHelper.showMessage(ResetPasswordActivity.this, "新密码不能小于6位");
            return;
        }
        if (confirmPassword.length() < 6) {
            ToastHelper.showMessage(ResetPasswordActivity.this, "确认密码不能小于6位");
            return;
        }
        if (newPassword.length() != confirmPassword.length()) {
            ToastHelper.showMessage(ResetPasswordActivity.this, "两次输入的密码不一致");
            return;
        }
        if (!Strings.isEquals(newPassword, confirmPassword)) {
            ToastHelper.showMessage(ResetPasswordActivity.this, "两次输入的密码不一致");
            return;
        }
        LoadingHelper.showMaterLoading(this, "请稍候");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postResetPassword(newPassword);
            }
        }, 1000);
    }


    /**
     * 提交修改密码请求
     */
    private void postResetPassword(String newPassword) {
        Call<BaseDTO> call = ApiUtil.getAttestation().postResetPassword(phone, newPassword, smsCode);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(ResetPasswordActivity.this, "密码重置成功");
                AppManager.finishActivity(ForgotPasswordActivity.class);
                finish();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ResetPasswordActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
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
        setBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    /**
     * 设置按钮是否可以点击
     */
    private void setBtnState() {
        binding.tvConfirmEdit.setEnabled(false);
        binding.tvConfirmEdit.setSelected(false);
        if (Strings.isEmpty(binding.etNewPassword.getText().toString().trim())) {
            return;
        }
        if (Strings.isEmpty(binding.etConfirmPassword.getText().toString().trim())) {
            return;
        }
        binding.tvConfirmEdit.setEnabled(true);
        binding.tvConfirmEdit.setSelected(true);
    }
}
