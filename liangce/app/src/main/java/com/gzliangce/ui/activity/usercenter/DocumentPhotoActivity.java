package com.gzliangce.ui.activity.usercenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityDocumentPhotoBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.dto.UploadImageDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.enums.UserType;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.PhotoUploadAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.dialog.PictureChoseDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import retrofit.Call;

/**
 * Created by Aaron on 1/21/16.
 * 文档资料-照片
 */
public class DocumentPhotoActivity extends BaseSwipeBackActivityBinding {
    private ActivityDocumentPhotoBinding binding;
    private PhotoUploadAdapter adapter;

    private String orderNumber, documentType;
    private int position;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document_photo);
    }

    @Override
    public void initView() {
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
        documentType = getIntent().getStringExtra(Constants.DOCUMENT_DATA_TYPE);
        adapter = new PhotoUploadAdapter(this, orderNumber, false);
        binding.rvPhotoList.setAdapter(adapter);
        binding.rvPhotoList.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        setHeader();
        getDocumentPhotoList();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle(LiangCeUtil.showDocumentTille(documentType));
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 获取文档资料列表
     */
    private void getDocumentPhotoList() {
        Map<String, String> map = new HashMap<>();
        map.put("number", orderNumber);
        map.put("type", documentType);
        Call<ListDTO<Images>> call = ApiUtil.getOrderService().getDocumentPhotoList(map);
        call.enqueue(new APICallback<ListDTO<Images>>() {
            @Override
            public void onSuccess(ListDTO<Images> imagesListDTO) {
                handleData(imagesListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(DocumentPhotoActivity.this, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }


    /**
     * 网络请求完成数据处理
     *
     * @param imagesList
     */
    private void handleData(List<Images> imagesList) {
        logger.e(imagesList.toString());
        adapter.clear();
        for (Images images : imagesList) {
            images.setUploaded(true);
            images.setSelected(true);
            adapter.add(images);
        }
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            addEmptyPic();
        }

        adapter.notifyDataSetChanged();
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
     * 上传图片弹窗
     */
    public void showPicDialog(int position) {
        this.position = position;
        PictureChoseDialog pictureChoseDialog = new PictureChoseDialog(this);
        pictureChoseDialog.show();
    }


    /**
     * 添加空图片对象，用于上传按钮的显示
     */
    private void addEmptyPic() {
        Images images = new Images();
        adapter.add(images);
    }

    /**
     * 上传资料图片
     *
     * @param file
     */
    private void uploadPic(File file) {
        LoadingHelper.showMaterLoading(this, "上传图片中...");
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody number = RequestBody.create(MediaType.parse("text/plain"), orderNumber);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), documentType);

        map.put("file\"; filename=\"" + PhotoUtil.getFileName() + "", body);
        map.put("number", number);
        map.put("type", type);
        Call<UploadImageDTO> call = ApiUtil.getOrderService().uploadDocumentPic(map);
        call.enqueue(new APICallback<UploadImageDTO>() {
            @Override
            public void onSuccess(UploadImageDTO uploadImageDTO) {
                handlerPicData(uploadImageDTO.getLink());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(DocumentPhotoActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    private void handlerPicData(String link) {
        adapter.get(position).setUrl(link);
        adapter.get(position).setSelected(true);
        adapter.get(position).setUploaded(true);
        addEmptyPic();
        adapter.notifyDataSetChanged();
        PhotoUtil.cleanPicPath();
    }
}
