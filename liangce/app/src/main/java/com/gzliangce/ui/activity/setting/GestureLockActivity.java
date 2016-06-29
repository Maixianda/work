package com.gzliangce.ui.activity.setting;

import android.databinding.DataBindingUtil;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityGestureActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.leo.gesturelibray.enums.LockMode;
import com.leo.gesturelibray.view.CustomLockView;

import io.ganguo.library.Config;
import io.ganguo.library.util.Strings;

import static com.leo.gesturelibray.enums.LockMode.CLEAR_PASSWORD;
import static com.leo.gesturelibray.enums.LockMode.SETTING_PASSWORD;

/**
 * Created by leo on 16/1/7.
 * 手势密码界面
 */
public class GestureLockActivity extends BaseSwipeBackActivityBinding {
    private ActivityGestureActivityBinding binding;
    private LockMode gestureLockMode;
    private Class verifyJumActivity;//当手势解锁已经设置，再开启PIN解锁，需要先验证关闭手势，再跳转到PIN设置activity

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gesture_activity);
        setHeader();

    }

    @Override
    public void initView() {
        getSwipeBackLayout().setEdgeSize(getResources().getDimensionPixelSize(R.dimen.dp_43));
        //显示绘制方向
        binding.lvLock.setShow(false);
        //允许最大输入次数
        binding.lvLock.setErrorNumber(5);
        //密码最少位数
        binding.lvLock.setPasswordMinLength(4);
        //编辑密码或设置密码时，是否将密码保存到本地，配合setSaveLockKey使用
        binding.lvLock.setSavePin(true);
        //保存密码Key
        binding.lvLock.setSaveLockKey(AppContext.me().getGestureKey());
    }

    @Override
    public void initListener() {
        binding.lvLock.setOnCompleteListener(onCompleteListener);
    }

    @Override
    public void initData() {
        verifyJumActivity = (Class) getIntent().getSerializableExtra(Constants.IS_UNLOCK_JUM_ACTIVITY_NAME);
        gestureLockMode = (LockMode) getIntent().getSerializableExtra(Constants.IS_GESTURE_LOCK_MODE);
        setLockMode(gestureLockMode);
        if (verifyJumActivity != null) {
            //如果是在设置手势解锁后，要进行PIN设置，验证手势密码成功后先不删除本地手势密码，待PIN设置完成后才清除手势密码
            binding.lvLock.setClearPasssword(false);
        }
    }


    /**
     * 设置解锁模式
     */
    private void setLockMode(LockMode mode) {
        String str;
        switch (mode) {
            case CLEAR_PASSWORD:
                str = "绘制手势";
                setLockMode(CLEAR_PASSWORD, getPin(), str);
                break;
            case EDIT_PASSWORD:
                str = "修改手势";
                setLockMode(LockMode.EDIT_PASSWORD, getPin(), str);
                binding.tvHint.setText("请绘制旧手势");
                break;
            case SETTING_PASSWORD:
                str = "设置手势";
                setLockMode(SETTING_PASSWORD, null, str);
                break;
            case VERIFY_PASSWORD:
                str = "绘制手势";
                setLockMode(LockMode.VERIFY_PASSWORD, getPin(), str);
                getSwipeBackLayout().setEnableGesture(false);
                header.setLeftIcon(0);
                header.setLeftBackground(0);
                header.setRightTitle("注销");
                break;
        }
    }


    /**
     * 密码输入模式
     */
    private void setLockMode(LockMode mode, String password, String msg) {
        if (mode != SETTING_PASSWORD) {
            binding.lvLock.setOldPassword(password);
        }
        binding.lvLock.setMode(mode);
        header.setMidTitle(msg);
    }


    /**
     * 密码输入监听
     */
    CustomLockView.OnCompleteListener onCompleteListener = new CustomLockView.OnCompleteListener() {
        @Override
        public void onComplete(String password, int[] indexs) {
            onCompleteHint();
        }

        @Override
        public void onError(String errorTimes) {
            binding.tvHint.setText("密码错误，还可以再输入" + errorTimes + "次");
            AnimUtil.startShakeAnimation(binding.tvHint);
        }

        @Override
        public void onPasswordIsShort(int passwordMinLength) {
            binding.tvHint.setText("请绘制至少" + passwordMinLength + "位手势");
            AnimUtil.startShakeAnimation(binding.tvHint);
        }

        @Override
        public void onAginInputPassword(LockMode mode, String password, int[] indexs) {
            binding.tvHint.setText("请再次绘制新手势");
            AnimUtil.startShakeAnimation(binding.tvHint);
        }

        @Override
        public void onInputNewPassword() {
            binding.tvHint.setText("请绘制新手势");
            AnimUtil.startShakeAnimation(binding.tvHint);
        }

        @Override
        public void onEnteredPasswordsDiffer() {
            binding.tvHint.setText("与上次绘制密码不一致，请再次绘制");
            AnimUtil.startShakeAnimation(binding.tvHint);
        }

        @Override
        public void onErrorNumberMany() {
            binding.tvHint.setText("错误次数过多，手势密码已失效");
            DialogUtil.showErrorPasswordDialog(GestureLockActivity.this);
            AnimUtil.startShakeAnimation(binding.tvHint);
        }
    };


    /**
     * 密码操作完成
     */
    private void onCompleteHint() {
        String hint = "";
        String msg = "";
        switch (gestureLockMode) {
            case SETTING_PASSWORD:
                msg = "手势密码设置成功";
                hint = "绘制完成";
                Config.remove(AppContext.me().getPinKey());
                break;
            case VERIFY_PASSWORD:
                hint = "密码输入正确";
                break;
            case EDIT_PASSWORD:
                msg = "手势密码修改成功";
                hint = "绘制完成";
                break;
            case CLEAR_PASSWORD:
                msg = "密码输入正确,手势密码已关闭";
                hint = "密码输入正确,手势密码已关闭";
                break;
        }
        if (verifyJumActivity != null) {
            msg = "";
            hint = "密码输入正确";
        }
        binding.tvHint.setText(hint);
        if (!Strings.isEmpty(msg)) {
            LiangCeUtil.showCenterToast(this, msg, true);
        }
        if (verifyJumActivity != null) {
            IntentUtil.actionLockStingActivity(this, verifyJumActivity);
        }
        finish();
    }


    /**
     * 获取已经设置过的密码
     */
    private String getPin() {
        String str = Config.getString(AppContext.me().getGestureKey());
        return str;
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

    @Override
    public void onBackPressed() {
        if (gestureLockMode == LockMode.VERIFY_PASSWORD) {
            DialogUtil.exitAppDialog(this);
            return;
        }
        super.onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    @Override
    protected void onDestroy() {
        AppContext.isBackGround = false;
        super.onDestroy();
    }
}
