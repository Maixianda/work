package com.gzliangce.ui.activity.usercenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityUserInfoBinding;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.dto.HeaderDTO;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.dialog.PictureChoseDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 个人信息界面
 */
public class UserInfoActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(UserInfoActivity.class);
    private ActivityUserInfoBinding binding;
    private AccountInfo userInfo;
    private PictureChoseDialog pictureChoseDialog;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.rlyHeader.setOnClickListener(this);
        binding.rlyUsername.setOnClickListener(this);
        binding.rlyLanguage.setOnClickListener(this);
        binding.rlyFunctions.setOnClickListener(this);
        binding.rlyDirections.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppContext.me().isLogined()) {
            userInfo = AppContext.me().getUser();
            binding.setAccount(userInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_header:
                pictureChoseDialog = new PictureChoseDialog(this);
                pictureChoseDialog.show();
                break;
            case R.id.rly_username:
                doIntent(UserNameActivity.class, 0);
                break;
            case R.id.rly_language:
                doIntent(UserSwipeActivity.class, 1);
                break;
            case R.id.rly_functions:
                doIntent(UserSwipeActivity.class, 2);
                break;
            case R.id.rly_directions:
                doIntent(UserDirectionsActivity.class, 0);
                break;

        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("个人信息");
        binding.setHeader(header);
    }

    /**
     * 例子：获取用户信息
     */
    private void getUserInfo() {
        userInfo = new AccountInfo();
        userInfo.setIcon("https://gitlab.cngump.com/uploads/user/avatar/41/7f121f48b4d0182a9c5f3a0f357d37f8.jpg");
        userInfo.setRealName("hulk");
        userInfo.setPhone("13823848321");
        binding.setAccount(userInfo);
    }

    private void doIntent(Class<?> classValues, int fromType) {
        Intent intent = new Intent(this, classValues);
        if (fromType != 0) {
            intent.putExtra(Constants.ACTIVITY_FROM_TYPE, fromType);
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照返回的结果
            case PhotoUtil.TAKE_PHOTO_VALUS:
                if (!PhotoUtil.getPicPath().exists()) {
                    return;
                }
                PhotoUtil.cropPhoto(this, Uri.parse("file:///" + PhotoUtil.getPicPath().getPath()), 720);
                break;
            //选择图库返回的结果
            case PhotoUtil.CHOSE_PHOTO_GALLERY_VALUES:
                if (data == null || data.getData() == null) {
                    return;
                }
                PhotoUtil.cropPhoto(this, data.getData(), 720);
                break;
            //调用截图返回的结果
            case PhotoUtil.CROP_PHOTO_VALUES:
                if (!PhotoUtil.getCropPicPath().exists()) {
                    return;
                }
                uploadPic(PhotoUtil.getCropPicPath());
                break;
        }
    }

    /**
     * 上传头像
     *
     * @param file
     */
    private void uploadPic(File file) {
        LoadingHelper.showMaterLoading(this, "上传头像中");
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        map.put("file\"; filename=\"" + PhotoUtil.getFileName() + "", body);
        Call<HeaderDTO> call = ApiUtil.getUserCenterService().uploadPic(map);
        call.enqueue(new APICallback<HeaderDTO>() {
            @Override
            public void onSuccess(HeaderDTO headerDTO) {
                handlerData(headerDTO.getUrl());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(UserInfoActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void handlerData(String headerUrl) {
        AccountInfo accountInfo = AppContext.me().getUser();
        accountInfo.setIcon(headerUrl);
        AppContext.me().setUser(accountInfo);
        userInfo.setIcon(headerUrl);
        binding.setAccount(userInfo);
        PhotoUtil.cleanPicPath();
        pictureChoseDialog.dismiss();
    }
}
