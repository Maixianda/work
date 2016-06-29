package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemOrderDataImageBinding;
import com.gzliangce.databinding.ItemPhotoUploadBinding;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.ui.activity.order.AdditionalMaterialsActivity;
import com.gzliangce.ui.activity.order.PhotoPreviewActivity;
import com.gzliangce.ui.activity.usercenter.DocumentPhotoActivity;
import com.gzliangce.ui.dialog.PictureChoseDialog;

import java.io.Serializable;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by zzwdream on 1/21/16.
 */
public class PhotoUploadAdapter extends ListAdapter<Images, ItemPhotoUploadBinding> {
    private Logger logger = LoggerFactory.getLogger(MyDataAdapter.class);
    private Activity activity;
    private String orderNumber;
    private boolean isAddtitionalMaterialsClass;

    public PhotoUploadAdapter(Activity activity, String orderNumber, boolean isAddtitionalMaterialsClass) {
        super(activity);
        this.activity = activity;
        this.orderNumber = orderNumber;
        this.isAddtitionalMaterialsClass = isAddtitionalMaterialsClass;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemPhotoUploadBinding> vh, int position) {
        Images images = get(position);
        if (!Strings.isEmpty(images.getUrl())) {
            PhotoLoader.display(vh.getBinding().ivPhoto, images.getUrl(), activity.getResources().getDrawable(R.drawable.shape_image_loading));
        } else {
            PhotoLoader.display(vh.getBinding().ivPhoto, images.getUrl(), activity.getResources().getDrawable(R.drawable.ic_empyt_upload));
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_photo_upload;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        switch (view.getId()) {
            case R.id.iv_photo:
                if (get(vh.getAdapterPosition()).isSelected()) {
                    Intent intent = new Intent(activity, PhotoPreviewActivity.class);
                    intent.putExtra(Constants.ORDER_PHOTO, (Serializable) getData());
                    intent.putExtra(Constants.PREVIEW_IMAGE_ACTIVITY_INDEX_DATA, vh.getAdapterPosition());
                    intent.putExtra(Constants.ORDER_NUMBER, orderNumber);
                    if (isAddtitionalMaterialsClass) {
                        intent.putExtra(Constants.IS_NEED_DELETE, true);
                    } else {
                        intent.putExtra(Constants.IS_NEED_DELETE, false);
                    }
                    activity.startActivity(intent)
                    ;
                } else {
                    if (isAddtitionalMaterialsClass) {
                        ((AdditionalMaterialsActivity) activity).showPicDialog(vh.getAdapterPosition());
                    } else {
                        ((DocumentPhotoActivity) activity).showPicDialog(vh.getAdapterPosition());
                    }
                }
                break;
        }
    }
}