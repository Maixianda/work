package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySignPhotoDetailsBinding;
import com.gzliangce.dto.ImagesDTO;
import com.gzliangce.dto.UploadImageDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.SignNewPhotoInfo;
import com.gzliangce.event.DeletePhotoEvent;
import com.gzliangce.event.SignNewPhotoEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.SignPhotoDetailsAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.widget.AutoHeightGradLayoutManager;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Files;
import retrofit.Call;


/**
 * Created by leo on 16/2/29.
 * 签约拍照详情界面
 */
public class SignPhotoDetailsActivity extends BaseSwipeBackActivityBinding {
    private ActivitySignPhotoDetailsBinding binding;
    private SignPhotoDetailsAdapter signPhotoAdapter;
    private SignNewPhotoInfo info;
    private String title = "与买家同照";

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_photo_details);
    }

    @Override
    public void initView() {
        signPhotoAdapter = new SignPhotoDetailsAdapter(this);
        binding.rcvPhoto.setLayoutManager(new AutoHeightGradLayoutManager(this, 3));
        binding.rcvPhoto.setAdapter(signPhotoAdapter);
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        info = (SignNewPhotoInfo) getIntent().getSerializableExtra(Constants.SIGN_NEW_PHOTO_INFO);
        if (info != null) {
            title = info.getTitle();
            binding.icdHeader.midHeader.setText(info.getTitle());
            signPhotoAdapter.setOrderNumber(info.getOrderNumber());
        }
        setHeader();
        signPhotoAdapter.add(new Images());
        signPhotoAdapter.notifyDataSetChanged();
        getUploadedPhoto();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_PHOTO_VALUS:  //拍照返回的结果
                if (!PhotoUtil.getPicPath().exists()) {
                    return;
                }
                PhotoUtil.cropPhoto(this, Uri.parse("file:///" + PhotoUtil.getPicPath().getPath()), 720);
                break;
            case PhotoUtil.CHOSE_PHOTO_GALLERY_VALUES:  //选择图库返回的结果
                if (data == null || data.getData() == null) {
                    return;
                }
                PhotoUtil.cropPhoto(this, data.getData(), 720);
                break;
            case PhotoUtil.CROP_PHOTO_VALUES://调用截图返回的结果
                if (!PhotoUtil.getCropPicPath().exists()) {
                    return;
                }
                uploadPic(PhotoUtil.getCropPicPath().getPath());
                break;
        }
    }

    /**
     * 处理图片数据
     */
    private void addImageData(List<Images> imagesList, UploadImageDTO dto) {
        signPhotoAdapter.clear();
        if (imagesList.size() >= 2) {
            signPhotoAdapter.addAll(imagesList.subList(0, imagesList.size() - 1));
        }
        Images images = new Images();
        images.setImageId(dto.getImageId());
        images.setUrl(dto.getLink());
        images.setPicLabel(info.getTitle());
        images.setUploaded(true);
        images.setSelected(true);
        signPhotoAdapter.add(images);
        signPhotoAdapter.add(new Images());
        signPhotoAdapter.notifyDataSetChanged();
        PhotoUtil.cleanPicPath();
    }


    /**
     * 获取签约照片
     */
    private void getUploadedPhoto() {
        LoadingHelper.showMaterLoading(this, "加载照片中...");
        Call<ImagesDTO> call = ApiUtil.getOrderService().getSignedPhoto(info.getOrderNumber(), info.getLable());
        call.enqueue(new APICallback<ImagesDTO>() {
            @Override
            public void onSuccess(ImagesDTO imagesDTO) {
                handlerData(imagesDTO.getImages());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SignPhotoDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }


    /**
     * 处理签约照片照片数据
     */
    private void handlerData(List<Images> imagesList) {
        signPhotoAdapter.clear();
        for (Images images : imagesList) {
            images.setUploaded(true);
            images.setSelected(true);
            images.setPicLabel(info.getTitle());
        }
        signPhotoAdapter.addAll(imagesList);
        signPhotoAdapter.add(new Images());
        signPhotoAdapter.notifyDataSetChanged();
    }


    /**
     * 上传资料图片
     *
     * @param imagesPath
     */
    private void uploadPic(String imagesPath) {
        if (!Files.checkFileExists(imagesPath)) {
            ToastHelper.showMessage(this, "文件不存在");
            return;
        }
        LoadingHelper.showMaterLoading(this, "正在上传照片");
        File picFile = new File(imagesPath);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), picFile);
        RequestBody number = RequestBody.create(MediaType.parse("text/plain"), info.getOrderNumber());
        RequestBody picLabel = RequestBody.create(MediaType.parse("text/plain"), info.getLable());
        map.put("file\"; filename=\"" + PhotoUtil.getFileName() + "", body);
        map.put("number", number);
        map.put("label", picLabel);
        Call<UploadImageDTO> call = ApiUtil.getOrderService().uploadSignPic(map);
        call.enqueue(new APICallback<UploadImageDTO>() {
            @Override
            public void onSuccess(UploadImageDTO uploadImageDTO) {
                logger.e("图片上传成功: " + uploadImageDTO.getLink());
                ToastHelper.showMessage(SignPhotoDetailsActivity.this, "图片上传成功");
                ArrayList<Images> imagesList = new ArrayList<>();
                imagesList.addAll(signPhotoAdapter.getData());
                addImageData(imagesList, uploadImageDTO);
            }

            @Override
            public void onFailed(String message) {
                LoadingHelper.hideMaterLoading();
                ToastHelper.showMessage(SignPhotoDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                EventHub.post(new SignNewPhotoEvent(info.getPosition(), true));
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setRightBackground(0);
        header.setMidTitle(title);
        binding.setHeader(header);
        header.onBackClickListener();
    }


    /**
     * 接收广播删除照片
     */
    @Subscribe
    public void onDeletePhoto(DeletePhotoEvent event) {
        signPhotoAdapter.remove(event.getPosition());
        signPhotoAdapter.notifyDataSetChanged();
        EventHub.post(new SignNewPhotoEvent(info.getPosition(), false));
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
