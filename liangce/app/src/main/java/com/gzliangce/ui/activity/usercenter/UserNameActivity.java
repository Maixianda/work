package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ActivityUserNameBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.HeaderDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 用户名界面
 */
public class UserNameActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(UserNameActivity.class);
    private ActivityUserNameBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_name);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.ivRemove.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (AppContext.me().isLogined()) {
            binding.setAccount(AppContext.me().getUser());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_remove:
                binding.etUserName.setText("");
                break;
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        saveUserName();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("用户名");
        header.setRightTitle("保存");
        binding.setHeader(header);
    }

    /**
     * 保存用户名
     */
    private void saveUserName() {
        if (Strings.isEmpty(binding.etUserName.getText() + "")) {
            ToastHelper.showMessage(this, "用户名不能为空");
            return;
        }
        if (binding.etUserName.getText().length() < 2 || binding.etUserName.getText().length() > 20) {
            ToastHelper.showMessage(this, "用户名范围2-20个字符");
            return;
        }
        LoadingHelper.showMaterLoading(this, "修改用户名中...");
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postUserName(binding.etUserName.getText() + "");
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handlerData();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(UserNameActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void handlerData() {
        ToastHelper.showMessage(UserNameActivity.this, "修改用户名成功");
        AccountInfo accountInfo = AppContext.me().getUser();
        accountInfo.setRealName(binding.etUserName.getText() + "");
        AppContext.me().setUser(accountInfo);
        finish();
    }

}
