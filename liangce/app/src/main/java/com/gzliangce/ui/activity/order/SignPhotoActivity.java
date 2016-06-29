package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySignPhotoBinding;
import com.gzliangce.dto.ImagesDTO;
import com.gzliangce.dto.UploadImageDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.event.DeletePhotoEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.SignPhotoAdapter;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.dialog.PictureChoseDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.widget.AutoHeightGradLayoutManager;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import retrofit.Call;

/**
 * 签约拍照界面
 * Created by aaron on 16/1/15.
 */
public class SignPhotoActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private ActivitySignPhotoBinding binding;
    private SignPhotoAdapter adapter;

    private String orderNumber;
    private int position = -1;
    private int sumUnupload = 0;
    private int sequence = 0;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_photo);
        setHeader();
    }

    @Override
    public void initView() {
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
        adapter = new SignPhotoAdapter(this, orderNumber);
        binding.rvPhotoList.setAdapter(adapter);
        binding.rvPhotoList.setLayoutManager(new AutoHeightGradLayoutManager(this, 3));
    }

    @Override
    public void initListener() {
        binding.tvUploadPic.setOnClickListener(this);
    }

    @Override
    public void initData() {
        for (int i = 0; i < 5; i++) {
            Images images = new Images();
            images.setPicLabel(returnLable(i));
            adapter.add(images);
        }
        adapter.notifyDataSetChanged();
        getUploadedPhoto();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload_pic:
                doCommitUpload();
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
        header.setMidTitle("签约拍照");
        header.setRightBackground(0);
        binding.setHeader(header);
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
                adapter.get(position).setUrl(PhotoUtil.getCropPicPath().getPath());
                adapter.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                PhotoUtil.cleanPicPath();
//                uploadPic(PhotoUtil.getCropPicPath());
                break;
        }
    }

    public void showPicDialog(int position) {
        this.position = position;
        PictureChoseDialog pictureChoseDialog = new PictureChoseDialog(this);
        pictureChoseDialog.show();
    }

    /**
     * 接收广播删除照片
     */
    @Subscribe
    public void onDeletePhoto(DeletePhotoEvent event) {
        Images images = new Images();
        images.setPicLabel(returnLable(event.getPosition()));
        adapter.set(event.getPosition(), images);
        adapter.notifyDataSetChanged();
    }

    private void doCommitUpload() {
        for (Images images : adapter.getData()) {
            if (images.isSelected()) {
                sumUnupload++;
            }
        }
        if (sumUnupload != adapter.getData().size()) {
            DialogUtil.showHintDialog(SignPhotoActivity.this, "请至少上传5张图片");
            return;
        }
        LoadingHelper.showMaterLoading(this, "上传图片中...");
        for (Images images : adapter.getData()) {
            if (images.isSelected()) {
                uploadPic(images);
            }
        }
    }

    /**
     * 获取签约照片
     */
    private void getUploadedPhoto() {
        LoadingHelper.showMaterLoading(this, "加载照片中...");
        Call<ImagesDTO> call = ApiUtil.getOrderService().getSignedPhoto(orderNumber);
        call.enqueue(new APICallback<ImagesDTO>() {
            @Override
            public void onSuccess(ImagesDTO imagesDTO) {
                handlerData(imagesDTO.getImages());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SignPhotoActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void handlerData(List<Images> imagesList) {
        adapter.clear();
        int imageSize = imagesList.size();
        logger.e("imageSize: " + imageSize);
        for (int i = 0; i < 5; i++) {
            if (i < imageSize) {
                imagesList.get(i).setUploaded(true);
                imagesList.get(i).setSelected(true);
                imagesList.get(i).setPicLabel(returnLable(i));
                adapter.add(imagesList.get(i));
            } else {
                Images images = new Images();
                images.setPicLabel(returnLable(i));
                adapter.add(images);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 上传资料图片
     *
     * @param images
     */
    private void uploadPic(Images images) {
        sequence++;
        File picFile = new File(images.getUrl());
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), picFile);
        RequestBody number = RequestBody.create(MediaType.parse("text/plain"), orderNumber);
        RequestBody picLabel = RequestBody.create(MediaType.parse("text/plain"), images.getPicLabel());
        map.put("file\"; filename=\"" + PhotoUtil.getFileName() + "", body);
        map.put("number", number);
        map.put("label", picLabel);
        Call<UploadImageDTO> call = ApiUtil.getOrderService().uploadSignPic(map);
        call.enqueue(new APICallback<UploadImageDTO>() {
            @Override
            public void onSuccess(UploadImageDTO uploadImageDTO) {
                logger.e("图片上传成功: " + uploadImageDTO.getLink());
            }

            @Override
            public void onFailed(String message) {
                LoadingHelper.hideMaterLoading();
                ToastHelper.showMessage(SignPhotoActivity.this, message);
            }

            @Override
            public void onFinish() {
                if (sequence == sumUnupload) {
                    ToastHelper.showMessage(SignPhotoActivity.this, "图片上传成功");
                    LoadingHelper.hideMaterLoading();
                    finish();
                }
            }
        });
    }

    private String returnLable(int position) {
        switch (position) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
        }
        return "a";
    }

}
