package com.gzliangce.ui.activity.attestation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityLoginBinding;
import com.gzliangce.db.DBManager;
import com.gzliangce.dto.UserDTO;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Regs;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.crypto.Rsas;
import retrofit.Call;

/**
 * Created by leo on 16/1/5.
 * 登录界面
 */
public class LoginActivity extends BaseSwipeBackActivityBinding implements TextWatcher, MaterialDialog.SingleButtonCallback {
    private ActivityLoginBinding binding;
    private boolean isActionMainActivity = false;//用于判断是否需要跳转到MainActivity

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setHeader();

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.tvLogin.setOnClickListener(onSingleClickListener);
        binding.tvRegistered.setOnClickListener(onSingleClickListener);
        binding.tvForgotPassword.setOnClickListener(onSingleClickListener);
        binding.tvRegistHint.setOnClickListener(onSingleClickListener);
        binding.etPassword.addTextChangedListener(this);
        binding.etPhoneNumber.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        header.setMidTitle("登录");
        isActionMainActivity = getIntent().getBooleanExtra(Constants.IS_ACTION_MANIN_ACTIVITY, false);
        if (isActionMainActivity) {
            getSwipeBackLayout().setEnableGesture(false);
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

    /**
     * 登录
     */
    private void actionLogin() {
        final String phone = binding.etPhoneNumber.getText().toString().trim();
        final String password = binding.etPassword.getText().toString().trim();
        if (!Regs.isPhone(phone)) {
            ToastHelper.showMessage(this, "手机号码不正确");
        } else if (password.length() < 6) {
            ToastHelper.showMessage(this, "密码不能少于6位");
        } else {
            actionPostLogin(phone, password);
        }
    }

    /**
     * 提交登录请求
     */
    private void actionPostLogin(final String phone, final String password) {
        LoadingHelper.showMaterLoading(this, "正在登录");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postLogin(phone, password);
            }
        }, 1000);
    }

    /**
     * 登录
     */
    private void postLogin(String phone, String password) {
        String psd = Rsas.getMD5(Rsas.getMD5(password) + phone);
        logger.e("psd: " + psd);
        Call<UserDTO> call = ApiUtil.getAttestation().postLogin("android", Systems.getDeviceId(this), phone, psd);
        call.enqueue(new APICallback<UserDTO>() {
            @Override
            public void onSuccess(UserDTO userDTO) {
                isActionMainActivity(userDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(LoginActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 登录成功后是否需要跳转到MainActivity
     */
    private void isActionMainActivity(final UserDTO userDTO) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DBManager.getInstance();
                AppContext.me().setUser(userDTO.getAccount());
                AppContext.me().setAvInstallation(userDTO.getAccount().getAccountId() + "");
                ToastHelper.showMessage(LoginActivity.this, "登录成功");
                if (isActionMainActivity) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                AppManager.finishOtherActivity(MainActivity.class);
                EventHub.post(new LoginEvent());
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
        setNextBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 判断是否满足下一步条件
     */
    private void setNextBtnState() {
        binding.tvLogin.setEnabled(false);
        binding.tvLogin.setSelected(false);
        if (Strings.isEmpty(binding.etPhoneNumber.getText().toString())) {
            return;
        }
        if (Strings.isEmpty(binding.etPassword.getText().toString())) {
            return;
        }
        binding.tvLogin.setSelected(true);
        binding.tvLogin.setEnabled(true);
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_login:
                    Systems.hideKeyboard(LoginActivity.this);
                    actionLogin();
                    break;
                case R.id.tv_registered:
                    IntentUtil.actionActivity(LoginActivity.this, RegisterActivity.class);
                    break;
                case R.id.tv_forgot_password:
                    IntentUtil.actionActivity(LoginActivity.this, ForgotPasswordActivity.class);
                    break;
                case R.id.tv_regist_hint:
                    IntentUtil.actionWebActivity(v.getContext(), "注册指引", 8);
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        if (isActionMainActivity) {
            DialogUtil.getMaterialDialog(this, getResources().getString(R.string.exit_Login_acitvity_text), this).show();
            return;
        }
        //清理未登录下单缓存
        CacheManager.disk(this).remove(Constants.SAVE_OREDER_INFO);
        super.onBackPressed();
    }


    /**
     * 确定退出登录界面
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
