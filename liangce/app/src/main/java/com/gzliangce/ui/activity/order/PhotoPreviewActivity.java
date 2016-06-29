package com.gzliangce.ui.activity.order;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityPhotoPreviewBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.entity.Images;
import com.gzliangce.event.DeletePhotoEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.PreviewPhotoAdapter;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * 图片预览界面
 * Created by aaron on 16/2/15.
 */
public class PhotoPreviewActivity extends BaseSwipeBackFragmentBinding implements MaterialDialog.SingleButtonCallback {
    public ActivityPhotoPreviewBinding binding;
    private List<Images> imagesList = new ArrayList<>();
    private PreviewPhotoAdapter adapter;

    private boolean isTitleHide = false;
    private int photoIndex, photoPosition;
    private String orderNumber;
    private boolean isSignPhoto, isNeedDelete;
    private List<Integer> hasPhoto = new ArrayList<>();
    private List<Images> list = new ArrayList<>();

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_preview);
    }

    @Override
    public void initView() {
        adapter = new PreviewPhotoAdapter(imagesList, this);
        binding.vpPhoto.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.vpPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                photoPosition = position;
                setHeader();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        handlerIntentData();
        binding.vpPhoto.setCurrentItem(photoPosition);
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        if (isNeedDelete) {
            DialogUtil.getMaterialDialog(this,
                    getResources().getString(R.string.m_action_delete_image),
                    this).show();
        }
    }

    /**
     * 处理传过来的数据
     */
    private void handlerIntentData() {
        list.clear();
        list = (List<Images>) getIntent().getSerializableExtra(Constants.ORDER_PHOTO);

        //获取有图片的有效数据
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                hasPhoto.add(i);
                imagesList.add(list.get(i));
            }
        }
        adapter.notifyDataSetChanged();

        photoIndex = getIntent().getIntExtra(Constants.PREVIEW_IMAGE_ACTIVITY_INDEX_DATA, 0);
        isSignPhoto = getIntent().getBooleanExtra(Constants.IS_SIGN_PHOTO, false);
        isNeedDelete = getIntent().getBooleanExtra(Constants.IS_NEED_DELETE, true);

        //保留图片位置
        for (int i = 0; i < hasPhoto.size(); i++) {
            if (hasPhoto.get(i) == photoIndex) {
                photoPosition = i;
                break;
            }
        }
        setHeader();
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        if (!isSignPhoto) {
            header.setMidTitle(photoPosition + 1 + "/" + imagesList.size());
        } else {
            String headTitle = imagesList.get(photoPosition).getPicLabel();
            header.setMidTitle(headTitle);
        }
        if (isNeedDelete) {
            header.setRightIcon(R.drawable.ic_delete);
        } else {
            header.setRightBackground(0);
        }
        binding.setHeader(header);
    }

    public void onPhotoClick() {
        if (isTitleHide) {
            isTitleHide = false;
            AnimUtil.getYAppendAnim(binding.icdHeader.rlyHeader, null).start();
        } else {
            isTitleHide = true;
            AnimUtil.getYSubtractAnim(binding.icdHeader.rlyHeader).start();
        }
    }


    /**
     * 删除图片
     */
    private void postDeletePhoto(Images images) {
        LoadingHelper.showMaterLoading(this, "删除图片中...");
        logger.e("deletePic: " + orderNumber + "   " + images.getImageId());

        Call<BaseDTO> call = null;

        if (!isSignPhoto) {
            call = ApiUtil.getOrderService().postDeletePic(orderNumber, images.getImageId() + "");
        } else {
            call = ApiUtil.getOrderService().postDeleteSignPic(orderNumber, images.getImageId() + "");
        }

        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handlerData();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PhotoPreviewActivity.this, message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }

    private void handlerData() {
        EventHub.post(new DeletePhotoEvent(hasPhoto.get(photoPosition)));
        imagesList.remove(photoPosition);
        hasPhoto.remove(photoPosition);
        if (photoPosition != 0) {
            photoPosition--;
        }
        if (imagesList.size() < 1) {
            onBackClicked();
            return;
        }
        adapter.notifyDataSetChanged();
        binding.vpPhoto.setAdapter(adapter);
        setHeader();
        binding.vpPhoto.setCurrentItem(photoPosition);
    }


    /**
     * Dialog 确定
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imagesList.get(photoPosition).isUploaded()) {
                    postDeletePhoto(imagesList.get(photoPosition));
                } else {
                    handlerData();
                }
            }
        });
    }
}
