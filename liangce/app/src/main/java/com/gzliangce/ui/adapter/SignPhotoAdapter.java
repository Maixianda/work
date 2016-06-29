package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemOrderDataImageBinding;
import com.gzliangce.entity.Images;
import com.gzliangce.ui.activity.order.AdditionalMaterialsActivity;
import com.gzliangce.ui.activity.order.PhotoPreviewActivity;
import com.gzliangce.ui.activity.order.SignPhotoActivity;

import java.io.Serializable;

import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by zzwdream on 1/21/16.
 */
public class SignPhotoAdapter extends ListAdapter<Images, ItemOrderDataImageBinding> {
    private Logger logger = LoggerFactory.getLogger(MyDataAdapter.class);
    private Activity activity;
    private String orderNumber;

    public SignPhotoAdapter(Activity activity, String orderNumber) {
        super(activity);
        this.activity = activity;
        this.orderNumber = orderNumber;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemOrderDataImageBinding> vh, int position) {
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_sign_photo;
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
                    intent.putExtra(Constants.IS_SIGN_PHOTO, true);
                    activity.startActivity(intent);
                } else {
                    ((SignPhotoActivity) activity).showPicDialog(vh.getAdapterPosition());
                }
                break;
        }
    }
}