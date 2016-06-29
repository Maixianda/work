package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityAdditionalMaterialsBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.SupplementDTO;
import com.gzliangce.dto.UploadImageDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.QualificationImageInfo;
import com.gzliangce.entity.Supplement;
import com.gzliangce.event.DeletePhotoEvent;
import com.gzliangce.event.DeletePreviewImageEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.PhotoUploadAdapter;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.dialog.PictureChoseDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 补充资料界面
 * Created by aaron on 16/1/15.
 */
public class AdditionalMaterialsActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(AdditionalMaterialsActivity.class);
    private ActivityAdditionalMaterialsBinding binding;

    private PhotoUploadAdapter adapter;
    private OrderInfo orderInfo;

    private int position = -1;
    private int sumUnupload = 0;
    private int sequence = 0;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_additional_materials);
        setHeader();
        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constants.ORDER_INFO);
    }

    @Override
    public void initView() {
        adapter = new PhotoUploadAdapter(this, orderInfo.getNumber(), true);
        binding.rvPhotoList.setAdapter(adapter);
        binding.rvPhotoList.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void initListener() {
        binding.tvSaveData.setOnClickListener(this);
    }

    @Override
    public void initData() {
        for (int i = 0; i < 3; i++) {
            Images images = new Images();
            adapter.add(images);
        }
        adapter.notifyDataSetChanged();
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_data:
                saveData();
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
        header.setMidTitle("补充资料");
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
//                adapter.get(position).setUrl(data.getDataString());
//                adapter.get(position).setSelected(true);
//                adapter.notifyDataSetChanged();
//                PhotoUtil.cleanPicPath();
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

    private void setPicToImages() {
        adapter.get(position).setUrl(PhotoUtil.getPicPath().getPath());
        adapter.get(position).setSelected(true);
        adapter.notifyDataSetChanged();
        PhotoUtil.cleanPicPath();
    }

    /**
     * 获取资料
     */
    private void getData() {
        LoadingHelper.showMaterLoading(this, "加载数据中...");
        Call<SupplementDTO> call = ApiUtil.getOrderService().getSupplement(orderInfo.getNumber());
        call.enqueue(new APICallback<SupplementDTO>() {
            @Override
            public void onSuccess(SupplementDTO supplementDTO) {
                handlerData(supplementDTO.getSupplement());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(AdditionalMaterialsActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void saveData() {
        LoadingHelper.showMaterLoading(this, "保存资料中...");
        for (Images images : adapter.getData()) {
            if (!images.isUploaded() && images.isSelected()) {
                sumUnupload++;
            }
        }

        for (Images images : adapter.getData()) {
            if (!images.isUploaded() && images.isSelected()) {
                File picFile = new File(images.getUrl());
                uploadPic(picFile);
            }
        }
        if (sumUnupload == 0) {
            saveTextData();
        }
    }

    /**
     * 上传资料图片
     *
     * @param file
     */
    private void uploadPic(File file) {
        sequence++;
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody number = RequestBody.create(MediaType.parse("text/plain"), orderInfo.getNumber());

        map.put("file\"; filename=\"" + PhotoUtil.getFileName() + "", body);
        map.put("number", number);
        Call<UploadImageDTO> call = ApiUtil.getOrderService().uploadSupplementPic(map);
        call.enqueue(new APICallback<UploadImageDTO>() {
            @Override
            public void onSuccess(UploadImageDTO uploadImageDTO) {
//                handlerPicData(uploadImageDTO);
            }

            @Override
            public void onFailed(String message) {
                LoadingHelper.hideMaterLoading();
                ToastHelper.showMessage(AdditionalMaterialsActivity.this, message);
            }

            @Override
            public void onFinish() {
                if (sequence == sumUnupload) {
                    saveTextData();
                }
            }
        });
    }

    /**
     * 保存资料
     */
    private void saveTextData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", orderInfo.getNumber());
        map.put("address", binding.etPropertyAddress.getText().toString());
        map.put("acreage", binding.etPropertyArea.getText().toString());
        map.put("loanPrice", binding.etLoanSum.getText().toString());
        Call<BaseDTO> call = ApiUtil.getOrderService().postSupplementData(map);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                ToastHelper.showMessage(AdditionalMaterialsActivity.this, "补充资料保存成功");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(AdditionalMaterialsActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
                finish();
            }
        });
    }

    private void handlerData(Supplement supplement) {
        logger.e("supplement: " + supplement.toString());
        adapter.clear();
        binding.etPropertyAddress.setText(supplement.getAddress());
        binding.etPropertyArea.setText(supplement.getAcreage());
        binding.etLoanSum.setText(supplement.getLoanPrice() + "");
        int imageSize = supplement.getImages().size();
        logger.e("imageSize: " + imageSize);
        for (int i = 0; i < 3; i++) {
            if (i < imageSize) {
                supplement.getImages().get(i).setUploaded(true);
                supplement.getImages().get(i).setSelected(true);
                adapter.add(supplement.getImages().get(i));
            } else {
                Images images = new Images();
                adapter.add(images);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void handlerPicData(UploadImageDTO uploadImageDTO) {
        Images images = adapter.get(position);
        images.setUrl(uploadImageDTO.getLink());
        images.setUploaded(true);
        images.setSelected(true);
        adapter.set(position, images);
        adapter.notifyDataSetChanged();
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
        adapter.set(event.getPosition(), images);
        adapter.notifyDataSetChanged();
    }
}
