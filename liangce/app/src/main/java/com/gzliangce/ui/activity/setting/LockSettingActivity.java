package com.gzliangce.ui.activity.setting;


import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.CompoundButton;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ActivityLockSettingBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.leo.gesturelibray.enums.LockMode;

import io.ganguo.library.Config;
import io.ganguo.library.util.Strings;

/**
 * Created by leo on 16/1/7.
 * 设置密码界面
 */
public class LockSettingActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityLockSettingBinding binding;
    public static String GestureKey;
    public static String PinKey;
    private boolean isSetState = true;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lock_setting);
        setHeader();
    }


    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.llEditGesture.setOnClickListener(this);
        binding.llEditPin.setOnClickListener(this);
        binding.sbGesture.setOnCheckedChangeListener(this);
        binding.sbPin.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        GestureKey = AppContext.me().getGestureKey();
        PinKey = AppContext.me().getPinKey();
        header.setMidTitle("锁屏设置");
        initPassWordButton();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }


    /**
     * 初始化按钮状态
     */
    public void initPassWordButton() {
        isSetState = true;
        String gestLock = Config.getString(GestureKey);
        String PinLock = Config.getString(PinKey);
        if (Strings.isNotEmpty(gestLock)) {
            binding.sbGesture.setChecked(true);
            binding.llEditGesture.setVisibility(View.VISIBLE);
        } else {
            binding.sbGesture.setChecked(false);
            binding.llEditGesture.setVisibility(View.GONE);
        }
        if (Strings.isNotEmpty(PinLock)) {
            binding.sbPin.setChecked(true);
            binding.llEditPin.setVisibility(View.VISIBLE);
        } else {
            binding.sbPin.setChecked(false);
            binding.llEditPin.setVisibility(View.GONE);
        }
        isSetState = false;
    }


    /**
     * 跳转到密码设置界面
     */
    private LockMode getLockMode(LockMode editMode, SwitchButton switchButton) {
        LockMode mode = LockMode.SETTING_PASSWORD;
        if (!switchButton.isChecked()) {
            mode = LockMode.CLEAR_PASSWORD;
        }
        if (editMode != null) {
            mode = editMode;
        }
        return mode;
    }


    /**
     * 判断开启密码前是否要关闭其他密码验证方式
     */
    private void isVerify(LockMode mode, Class mClass) {
        Class unLockJumActivity = null;
        if (Strings.isEquals(mClass.getName(), PinLockActivity.class.getName())) {
            if (binding.sbGesture.isChecked()) {
                unLockJumActivity = mClass;
                mClass = GestureLockActivity.class;
                mode = LockMode.CLEAR_PASSWORD;
            }
        } else if (Strings.isEquals(mClass.getName(), GestureLockActivity.class.getName())) {
            if (binding.sbPin.isChecked()) {
                unLockJumActivity = mClass;
                mClass = PinLockActivity.class;
                mode = LockMode.CLEAR_PASSWORD;
            }
        }
        IntentUtil.actionLockActivity(this, mode, mClass, unLockJumActivity);
    }


    @Override
    protected void onResume() {
        initPassWordButton();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_edit_gesture:
                IntentUtil.actionLockActivity(this, LockMode.EDIT_PASSWORD, GestureLockActivity.class, null);
                break;
            case R.id.ll_edit_pin:
                IntentUtil.actionLockActivity(this, LockMode.EDIT_PASSWORD, PinLockActivity.class, null);
                break;
        }
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isSetState) {
            return;
        }
        if (buttonView.getId() == R.id.sb_pin) {
            isVerify(getLockMode(null, binding.sbPin), PinLockActivity.class);
        } else {
            isVerify(getLockMode(null, binding.sbGesture), GestureLockActivity.class);
        }
    }
}
