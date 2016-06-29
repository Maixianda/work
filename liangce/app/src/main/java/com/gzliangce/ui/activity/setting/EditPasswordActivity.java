package com.gzliangce.ui.activity.setting;

import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ActivityEditPasswordBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.crypto.Rsas;
import retrofit.Call;

/**
 * Created by leo on 16/1/13.
 * 修改密码
 */
public class EditPasswordActivity extends BaseSwipeBackActivityBinding implements TextWatcher {
    private ActivityEditPasswordBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.etAgainInputPassword.addTextChangedListener(this);
        binding.etInputNewPassword.addTextChangedListener(this);
        binding.etOldPassword.addTextChangedListener(this);
        binding.tvConfirmEdit.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        header.setMidTitle("修改密码");
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
            actionEditPassword();
        }
    };


    /**
     * 判断是否满足修改密码条件
     */
    private void actionEditPassword() {
        final String oldPsd = binding.etOldPassword.getText().toString().trim();
        final String newPsd = binding.etInputNewPassword.getText().toString().trim();
        String againPsd = binding.etAgainInputPassword.getText().toString().trim();
        if (LiangCeUtil.passwdCheck(oldPsd) || LiangCeUtil.passwdCheck(newPsd)) {
            ToastHelper.showMessage(this, "密码中不允许存在中文");
            return;
        }
        if (oldPsd.length() < 6) {
            ToastHelper.showMessage(this, "旧密码不能少于6位");
            return;
        }
        if (newPsd.length() != againPsd.length()) {
            ToastHelper.showMessage(this, "两次输入的新密码不一致");
            return;
        }
        if (newPsd.length() < 6) {
            ToastHelper.showMessage(this, "新密码不能少于6位");
            return;
        }
        if (!Strings.isEquals(newPsd, againPsd)) {
            ToastHelper.showMessage(this, "两次输入的新密码不一致");
            return;
        }
        LoadingHelper.showMaterLoading(this, "请稍后");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postEditPassword(oldPsd, newPsd);
            }
        }, 100);

    }

    /**
     * 提交编辑密码请求
     */
    private void postEditPassword(String oldPsd, String newPsd) {
        oldPsd = Rsas.getMD5(Rsas.getMD5(oldPsd) + AppContext.me().getUser().getPhone());
        Call<BaseDTO> call = ApiUtil.getAttestation().postEditPassword(oldPsd, newPsd);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(EditPasswordActivity.this, "密码修改成功");
                finish();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(EditPasswordActivity.this, message);
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
     * 设置按钮状态
     */
    private void setBtnState() {
        binding.tvConfirmEdit.setEnabled(false);
        binding.tvConfirmEdit.setSelected(false);
        if (Strings.isEmpty(binding.etOldPassword.getText().toString().trim())) {
            return;
        } else if (Strings.isEmpty(binding.etInputNewPassword.getText().toString().trim())) {
            return;
        } else if (Strings.isEmpty(binding.etAgainInputPassword.getText().toString().trim())) {
            return;
        }
        binding.tvConfirmEdit.setEnabled(true);
        binding.tvConfirmEdit.setSelected(true);
    }
}
