package com.gzliangce.ui.activity.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityPinPasswordBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.PasswordType;
import com.leo.gesturelibray.enums.LockMode;

import io.ganguo.library.Config;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.crypto.Base64;

/**
 * Created by leo on 16/1/8.
 * 设置PIN码界面
 */
public class PinLockActivity extends BaseSwipeBackActivityBinding {
    private ActivityPinPasswordBinding binding;
    private LockMode gestureLockMode;
    private String mPassword = null;
    private boolean isTrue = false;//用于判断修改密码之前的验证是否成功
    private Class verifyJumActivity;//当PIN解锁已经设置，再开启手势解锁，需要先验证关闭PIN码，再跳转到的手势设置activity
    private int errorTimes = 5;//允许最大输错次数


    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_password);
        setHeader();
    }

    @Override
    public void initView() {
        binding.gpvCustomUi.setPasswordType(PasswordType.NUMBER);
    }

    @Override
    public void initListener() {
        binding.gpvCustomUi.setOnPasswordChangedListener(onPasswordChangedListener);
    }

    @Override
    public void initData() {
        initPasswordState();
    }


    /**
     * 初始化密码状态设置
     */
    private void initPasswordState() {
        gestureLockMode = (LockMode) getIntent().getSerializableExtra(Constants.IS_GESTURE_LOCK_MODE);
        verifyJumActivity = (Class) getIntent().getSerializableExtra(Constants.IS_UNLOCK_JUM_ACTIVITY_NAME);
        if (gestureLockMode == LockMode.SETTING_PASSWORD) {
            header.setMidTitle("设置密码");
            binding.tvHint.setText("请输入密码");
        } else if (gestureLockMode == LockMode.VERIFY_PASSWORD) {
            getSwipeBackLayout().setEnableGesture(false);
            header.setLeftIcon(0);
            header.setLeftBackground(0);
            header.setMidTitle("输入密码");
            header.setRightTitle("注销");
            getPinPassWord();
        } else if (gestureLockMode == LockMode.CLEAR_PASSWORD) {
            header.setMidTitle("输入密码");
            getPinPassWord();
        } else {
            header.setMidTitle("修改密码");
            binding.tvHint.setText("请输入旧密码");
            getPinPassWord();
        }
    }


    /**
     * 获取密码
     */
    private void getPinPassWord() {
        if (Strings.isEmpty(mPassword)) {
            mPassword = Config.getString(AppContext.me().getPinKey());
        }
    }


    /**
     * 设置密码
     */
    private void actionSettingPinLock(String psw, String hint) {
        if (Strings.isEmpty(mPassword)) {
            mPassword = Base64.encode(psw.getBytes());
            binding.tvHint.setText(hint);
            AnimUtil.startShakeAnimation(binding.tvHint);
        } else {
            if (Strings.isEquals(mPassword, Base64.encode(psw.getBytes()))) {
                String str = "密码设置成功";
                String msg = "PIN密码设置成功";
                if (gestureLockMode == LockMode.EDIT_PASSWORD) {
                    str = "密码设置成功";
                    msg = "PIN密码修改成功";
                }
                binding.tvHint.setText(str);
                Config.remove(AppContext.me().getGestureKey());//删除手势密码
                Config.putString(AppContext.me().getPinKey(), mPassword);
                LiangCeUtil.showCenterToast(this, msg, true);
                finish();
            } else {
                binding.tvHint.setText("两次输入密码不一致，请重试");
                AnimUtil.startShakeAnimation(binding.tvHint);
            }
        }
    }


    /**
     * 密码输入监听
     */
    private GridPasswordView.OnPasswordChangedListener onPasswordChangedListener = new GridPasswordView.OnPasswordChangedListener() {
        @Override
        public void onTextChanged(String psw) {

        }

        @Override
        public void onInputFinish(String psw) {
            logger.e("密码输入完毕");
            actionPasswordFinish(psw);
        }
    };

    /**
     * 密码输入完毕判断
     */
    private void actionPasswordFinish(String psw) {
        if (gestureLockMode == LockMode.EDIT_PASSWORD) {
            if (!isTrue) {
                actionValidationPin(psw);
            } else {
                clearInputPassword();
                actionSettingPinLock(psw, "请再次输入密码");
            }
        } else if (gestureLockMode == LockMode.SETTING_PASSWORD) {
            clearInputPassword();
            actionSettingPinLock(psw, "请再次输入密码");
        } else {
            actionValidationPin(psw);
        }
    }

    /**
     * 解锁发生错误
     */
    private void unLocKError() {
        errorTimes--;
        if (errorTimes <= 0) {
            binding.tvHint.setText("PIN密码已经失效");
            DialogUtil.showErrorPasswordDialog(this);
        } else {
            binding.tvHint.setText("密码错误，还可以输入" + errorTimes + "次");
            clearInputPassword();
        }
        AnimUtil.startShakeAnimation(binding.tvHint);
    }


    /**
     * 判断密码输入是否正确
     */
    private void actionValidationPin(String psw) {
        if (Strings.isEquals(mPassword, Base64.encode(psw.getBytes()))) {
            actionOnComplete();
        } else {
            unLocKError();
        }
    }


    /**
     * 密码输入正确后的操作
     */
    private void actionOnComplete() {
        if (gestureLockMode == LockMode.VERIFY_PASSWORD) {
            actionUnLockActivity();
        } else if (gestureLockMode == LockMode.CLEAR_PASSWORD && verifyJumActivity == null) {
            //关闭密码
            binding.tvHint.setText("PIN密码验证已关闭");
            LiangCeUtil.showCenterToast(this, "PIN密码验证已关闭", true);
            Config.remove(AppContext.me().getPinKey());
            finish();
        } else if (gestureLockMode == LockMode.CLEAR_PASSWORD && verifyJumActivity != null) {
            //关闭密码
            binding.tvHint.setText("PIN密码正确");
            IntentUtil.actionLockStingActivity(this, verifyJumActivity);
        } else {
            clearInputPassword();
            binding.tvHint.setText("请输入新密码");
            mPassword = null;
            isTrue = true;
            AnimUtil.startShakeAnimation(binding.tvHint);
        }
    }

    /**
     * 解锁成功后的操作
     */
    private void actionUnLockActivity() {
        //解锁成功后需要跳转的界面代码，写这里
        binding.tvHint.setText("密码正确");
        if (verifyJumActivity != null) {
            startActivity(new Intent(this, verifyJumActivity));
        }
        finish();
    }


    @Override
    public void onMenuClicked() {
        DialogUtil.LogOutDialog(this);
    }


    @Override
    public void onBackClicked() {
        if (gestureLockMode == LockMode.VERIFY_PASSWORD) {
            return;
        }
        onBackPressed();
    }

    /**
     * 延时清除已经输入的密码
     */
    private void clearInputPassword() {
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.gpvCustomUi.clearPassword();
            }
        }, 300);
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    @Override
    public void onBackPressed() {
        if (gestureLockMode == LockMode.VERIFY_PASSWORD) {
            DialogUtil.exitAppDialog(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        AppContext.isBackGround = false;
        super.onDestroy();
    }
}
