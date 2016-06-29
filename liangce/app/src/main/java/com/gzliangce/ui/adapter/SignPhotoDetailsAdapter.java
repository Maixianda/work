package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemSignPhotoDetailsBinding;
import com.gzliangce.entity.Images;
import com.gzliangce.ui.activity.order.PhotoPreviewActivity;
import com.gzliangce.ui.dialog.PictureChoseDialog;

import java.io.Serializable;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Strings;

/**
 * Created by leo on 16/2/29.
 * 签约拍照详情 - adapter
 */
public class SignPhotoDetailsAdapter extends ListAdapter<Images, ItemSignPhotoDetailsBinding> {
    private Activity activity;
    private PictureChoseDialog pictureChoseDialog;
    private String orderNumber;

    public SignPhotoDetailsAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemSignPhotoDetailsBinding> vh, final int position) {
        if (!Strings.isEmpty(get(position).getUrl())) {
            //设置有图片时的加载背景
            vh.getBinding().ivPhoto.setBackground(activity.getResources().getDrawable(R.drawable.shape_image_loading));
        } else {
            //设置上传图标
            vh.getBinding().ivPhoto.setBackgroundResource(R.drawable.ic_empyt_upload);
        }
        vh.getBinding().ivPhoto.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (position == getItemCount() - 1) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getContext(), PhotoPreviewActivity.class);
                    intent.putExtra(Constants.ORDER_PHOTO, (Serializable) getData());
                    intent.putExtra(Constants.PREVIEW_IMAGE_ACTIVITY_INDEX_DATA, vh.getAdapterPosition());
                    intent.putExtra(Constants.ORDER_NUMBER, orderNumber);
                    intent.putExtra(Constants.IS_SIGN_PHOTO, true);
                    activity.startActivity(intent);
                }
            }
        });
    }


    /**
     * 显示dialog
     */
    private void showDialog() {
        if (pictureChoseDialog == null) {
            pictureChoseDialog = new PictureChoseDialog(activity);
        }
        pictureChoseDialog.show();
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_sign_photo_details;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
