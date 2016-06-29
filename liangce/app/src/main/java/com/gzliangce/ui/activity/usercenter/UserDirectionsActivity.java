package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ActivityUserDirectionsBinding;
import com.gzliangce.databinding.ActivityUserNameBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;

import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 个人说明界面
 */
public class UserDirectionsActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(UserDirectionsActivity.class);
    private ActivityUserDirectionsBinding binding;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_directions);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.etDirectionsContent.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        updateDatum();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("个人说明");
        header.setRightTitle("保存");
        binding.setHeader(header);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        if (AppContext.me().isLogined()) {
            AccountInfo accountInfo = AppContext.me().getUser();
            binding.setAccount(accountInfo);
        }
    }

    /**
     * 输入监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int allowEidtwords = 70 - s.length();
            binding.tvPrompt.setText(allowEidtwords + "");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 更新个人说明
     */
    public void updateDatum() {
        if (Strings.isEmpty(binding.etDirectionsContent.getText() + "")) {
            ToastHelper.showMessage(this, "个人说明不能为空");
            return;
        }
        if (binding.etDirectionsContent.length() > 70) {
            ToastHelper.showMessage(UserDirectionsActivity.this, "字数超过限制");
            return;
        }
        LoadingHelper.showMaterLoading(UserDirectionsActivity.this, "个人说明保存中...");
        Map<String, String> map = new HashMap<>();
        map.put("introduce", binding.etDirectionsContent.getText() + "");
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postUserDatum(map);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handlerData();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(UserDirectionsActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 处理接口成功数据
     */
    private void handlerData() {
        AccountInfo accountInfo = AppContext.me().getUser();
        accountInfo.getInfo().setIntroduce(binding.etDirectionsContent.getText() + "");
        AppContext.me().setUser(accountInfo);
        ToastHelper.showMessage(UserDirectionsActivity.this, "个人说明保存成功");
        finish();
    }

}
