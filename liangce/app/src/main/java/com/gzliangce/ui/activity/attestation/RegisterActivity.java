package com.gzliangce.ui.activity.attestation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityRegisteredBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.VerifyCodeDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.CodeUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.TimeCount;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Regs;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/1/6.
 * 注册界面
 */
public class RegisterActivity extends BaseSwipeBackActivityBinding implements TimeCount.TimeCountListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    private ActivityRegisteredBinding binding;
    private TimeCount timeCount;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registered);
        setHeader();
    }

    @Override
    public void initView() {
        timeCount = new TimeCount(Constants.ILLIS_IN_FUTURE, Constants.COUNT_DOWN_INTERVAL, this);
    }

    @Override
    public void initListener() {
        binding.tvNext.setOnClickListener(onSingleClickListener);
        binding.ivImageCode.setOnClickListener(onSingleClickListener);
        binding.tvGetCode.setOnClickListener(onSingleClickListener);
        binding.etSmsCode.addTextChangedListener(this);
        binding.etImageCode.addTextChangedListener(this);
        binding.etPhoneNumber.addTextChangedListener(this);
        binding.tvUserHint.setOnClickListener(onSingleClickListener);
        binding.etRecommendPhoneNumber.addTextChangedListener(this);
        binding.ckIsAgreed.setOnCheckedChangeListener(this);
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        header.setMidTitle("注册");
        getVerifyCode();
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
                case R.id.tv_next:
                    actionIsConditions(false);
                    break;
                case R.id.tv_get_code:
                    actionIsConditions(true);
                    break;
                case R.id.iv_image_code:
                    getVerifyCode();
                    break;
                case R.id.tv_user_hint:
                    IntentUtil.actionWebActivity(RegisterActivity.this, "用户协议", 4);
                    break;
            }
        }
    };

    /**
     * 获取图文验证码
     */
    private void getVerifyCode() {
        Call<VerifyCodeDTO> call = ApiUtil.getAttestation().getVerifyCode(Systems.getDeviceId(this));
        call.enqueue(new APICallback<VerifyCodeDTO>() {
            @Override
            public void onSuccess(VerifyCodeDTO verifyCodeDTO) {
                handlerData(verifyCodeDTO);
                logger.i("获取验证码成功" + verifyCodeDTO.toString());
            }

            @Override
            public void onFailed(String message) {
                logger.i("获取验证码失败:" + message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 处理图文验证码
     */
    private void handlerData(final VerifyCodeDTO verifyCodeDTO) {
        if (verifyCodeDTO == null) {
            return;
        }
        Bitmap bitmap = CodeUtil.getInstance().createBitmap(RegisterActivity.this, verifyCodeDTO.getVerifyCode());
        binding.ivImageCode.setImageBitmap(bitmap);
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = CodeUtil.getInstance().createBitmap(RegisterActivity.this, verifyCodeDTO.getVerifyCode());
                binding.ivImageCode.setImageBitmap(bitmap);
            }
        });
    }


    /**
     * 获取短信验证码
     */
    private void actionIsConditions(boolean isGetSms) {
        String phone = binding.etPhoneNumber.getText().toString();
        String verifyCode = binding.etImageCode.getText().toString();
        String referrerPhone = binding.etRecommendPhoneNumber.getText().toString().trim();
        if (Strings.isEmpty(phone)) {
            ToastHelper.showMessage(this, "请先输入手机号码");
        } else if (!Regs.isPhone(phone)) {
            ToastHelper.showMessage(this, "手机号码格式不合法");
        } else if (Strings.isEmpty(verifyCode)) {
            ToastHelper.showMessage(this, "请先输入图文验证码");
        } else if (!Strings.isEmpty(referrerPhone) && !Regs.isPhone(referrerPhone)) {
            ToastHelper.showMessage(this, "推荐人手机号码格式不合法");
        } else if (isGetSms) {
            binding.tvGetCode.setSelected(true);
            binding.tvGetCode.setEnabled(false);
            timeCount.start();
            getSmsCode(phone, verifyCode);
        } else {
            actionNext(phone, referrerPhone);
        }
    }


    /**
     * 短信验证码
     */
    private void getSmsCode(String phone, String verifyCode) {
        Call<BaseDTO> call = ApiUtil.getAttestation().getSmsCode(Systems.getDeviceId(this), phone, verifyCode);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
            }

            @Override
            public void onFailed(String message) {
                timeCount.cancel();
                timeCount.onFinish();
                getVerifyCode();
                ToastHelper.showMessage(RegisterActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 点击下一步
     */
    private void actionNext(final String phone, final String referrerPhone) {
        LoadingHelper.showMaterLoading(RegisterActivity.this, "请稍后");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postValidateSms(phone, referrerPhone);
            }
        }, 1000);
    }


    /**
     * 判断验证码是否正确
     */
    private void postValidateSms(final String phone, final String referrerPhone) {
        final String code = binding.etSmsCode.getText().toString().trim();
        ApiUtil.postValidateSms(phone, code, new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                actionSettingpassWord(phone, code, referrerPhone);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(RegisterActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 跳转到设置密码界面
     */
    private void actionSettingpassWord(String phone, String code, String referrerPhone) {
        Intent intent = new Intent(RegisterActivity.this, SettingPassWordActivity.class);
        intent.putExtra(Constants.USER_PHONE_NUMBER, phone);
        intent.putExtra(Constants.USER_SMS_CODE, code);
        intent.putExtra(Constants.USER_REFERRER_PHONE_NUMBER, referrerPhone);
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
        setNextBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 判断是否满足下一步条件
     */
    private void setNextBtnState() {
        binding.tvNext.setEnabled(false);
        binding.tvNext.setSelected(false);
        if (Strings.isEmpty(binding.etPhoneNumber.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etImageCode.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etSmsCode.getText().toString())) {
            return;
        }
        if (!binding.ckIsAgreed.isChecked()) {
            return;
        }
        binding.tvNext.setSelected(true);
        binding.tvNext.setEnabled(true);
    }


    /**
     * CheckButton按钮
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setNextBtnState();
    }
}
