package com.gzliangce.ui.activity.attestation;

import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySettingPasswordBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.enums.UserType;
import com.gzliangce.http.APICallback;
import com.gzliangce.http.HttpMd5Encrypt;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import java.util.HashMap;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/1/11.
 * 设置密码
 */
public class SettingPassWordActivity extends BaseSwipeBackActivityBinding implements RadioGroup.OnCheckedChangeListener, TextWatcher {
    private Logger logger = LoggerFactory.getLogger(SettingPassWordActivity.class);
    private ActivitySettingPasswordBinding binding;
    private UserType userType = UserType.simpleUser;
    private String phoneNumber, code, referrerPhone;
    private HashMap<String, String> apiBody = new HashMap<>();

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_password);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.etUserName.addTextChangedListener(this);
        binding.etConfirmPassword.addTextChangedListener(this);
        binding.etSettingPassword.addTextChangedListener(this);
        binding.rgUserType.setOnCheckedChangeListener(this);
        binding.tvComplete.setOnClickListener(onSingleClickListener);
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        binding.rbPersonalUser.setChecked(true);
        header.setMidTitle("设置密码");
        referrerPhone = getIntent().getStringExtra(Constants.USER_REFERRER_PHONE_NUMBER);
        phoneNumber = getIntent().getStringExtra(Constants.USER_PHONE_NUMBER);
        code = getIntent().getStringExtra(Constants.USER_SMS_CODE);
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.rb_organization_user:
                userType = UserType.mediator;
                break;
            case R.id.rb_personal_user:
                userType = UserType.simpleUser;
                break;
        }
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            actionMeetTheConditions();
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
        setCompleteBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 判断是否满足注册条件
     */
    private void actionMeetTheConditions() {
        String settingPassword = binding.etSettingPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        String userName = binding.etUserName.getText().toString();
        if (LiangCeUtil.passwdCheck(settingPassword) || LiangCeUtil.passwdCheck(confirmPassword)) {
            ToastHelper.showMessage(this, "密码中不允许存在中文");
            return;
        }
        if (settingPassword.length() != confirmPassword.length()) {
            ToastHelper.showMessage(this, "两次输入的密码不一致");
            return;
        }
        if (!Strings.isEquals(settingPassword, confirmPassword)) {
            ToastHelper.showMessage(this, "两次输入的密码不一致");
            return;
        }
        actionRegister(settingPassword, userName);
    }


    /**
     * 延时提交注册请求
     */
    private void actionRegister(final String settingPassword, final String userName) {
        LoadingHelper.showMaterLoading(this, "请稍候");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postRegister(settingPassword, userName);
            }
        }, 1000);
    }

    /**
     * 提交注册请求
     */
    private void postRegister(String settingPassword, String userName) {
        addTextBody(settingPassword, userName);
        Call<BaseDTO> call = ApiUtil.getAttestation().postRegister(HttpMd5Encrypt.encryptyMap(apiBody));
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                AppManager.finishActivity(RegisterActivity.class);
                ToastHelper.showMessage(SettingPassWordActivity.this, "注册成功");
                finish();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SettingPassWordActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 添加请求字段
     */
    private void addTextBody(String settingPassword, String userName) {
        apiBody.clear();
        apiBody.put("phone", phoneNumber);
        apiBody.put("password", settingPassword);
        apiBody.put("code", code);
        apiBody.put("deviceId", Systems.getDeviceId(this));
        apiBody.put("os", "android");
        apiBody.put("type", userType.toString());
        apiBody.put("realName", userName);
        apiBody.put("referrer", referrerPhone);
    }


    /**
     * 设置完成按钮是否可以点击
     */
    private void setCompleteBtnState() {
        binding.tvComplete.setEnabled(false);
        binding.tvComplete.setSelected(false);
        if (Strings.isEmpty(binding.etSettingPassword.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etConfirmPassword.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etUserName.getText().toString())) {
            return;
        }
        binding.tvComplete.setEnabled(true);
        binding.tvComplete.setSelected(true);
    }
}
