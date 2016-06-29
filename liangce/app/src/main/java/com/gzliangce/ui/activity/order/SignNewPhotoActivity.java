package com.gzliangce.ui.activity.order;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySignNewPhotoBinding;
import com.gzliangce.dto.ImagesDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.SignNewPhotoInfo;
import com.gzliangce.event.SignNewPhotoEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.SignPhotoNewAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import retrofit.Call;

/**
 * Created by leo on 16/2/27.
 * 签约拍照-列表界面
 */
public class SignNewPhotoActivity extends BaseSwipeBackActivityBinding {
    private ActivitySignNewPhotoBinding binding;
    private SignPhotoNewAdapter adapter;
    private String orderNumber;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_new_photo);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new SignPhotoNewAdapter(this);
        binding.rvSignPhoto.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSignPhoto.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        addData();
    }

    private void addData() {
        adapter.add(new SignNewPhotoInfo("与买家同照", "", "buyer"));
        adapter.add(new SignNewPhotoInfo("与业主同照", "", "seller"));
        adapter.add(new SignNewPhotoInfo("与业主代理人同照", "", "sellerAgent"));
        adapter.add(new SignNewPhotoInfo("与买方代理人同照", "", "buyerAgent"));
        adapter.add(new SignNewPhotoInfo("与银行经办同照", "", "bankHandle"));
        adapter.notifyDataSetChanged();
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
        adapter.setOrderNumber(orderNumber);
        getUploadedPhoto();
    }


    /**
     * 获取签约照片缩略图
     */
    private void getUploadedPhoto() {
        LoadingHelper.showMaterLoading(this, "加载照片中...");
        Call<ImagesDTO> call = ApiUtil.getOrderService().getSignedLabePhoto(orderNumber);
        call.enqueue(new APICallback<ImagesDTO>() {
            @Override
            public void onSuccess(ImagesDTO imagesDTO) {
                handlerData(imagesDTO.getImages());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SignNewPhotoActivity.this, message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }

    /**
     * 处理缩略图
     */
    private void handlerData(List<Images> imagesList) {
        initListImageData();
        for (Images images : imagesList) {
            switch (images.getType()) {
                case "buyer":
                    adapter.get(0).setImage(images.getUrl());
                    break;
                case "seller":
                    adapter.get(1).setImage(images.getUrl());
                    break;
                case "buyerAgent":
                    adapter.get(3).setImage(images.getUrl());
                    break;
                case "sellerAgent":
                    adapter.get(2).setImage(images.getUrl());
                    break;
                case "bankHandle":
                    adapter.get(4).setImage(images.getUrl());
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化缩略图数据
     */
    private void initListImageData() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.get(i).setImage("");
        }
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
    public void onBackClicked() {
        onBackPressed();
    }


    /**
     * 刷新照片列表数据
     */
    @Subscribe
    public void onSignNewPhotoEvent(SignNewPhotoEvent event) {
        if (event == null) {
            return;
        }
        if (Strings.isEmpty(adapter.get(event.getPosition()).getImage())) {
            getUploadedPhoto();
        }
        if (!event.isUpLoad()) {
            getUploadedPhoto();
        }
    }
}
