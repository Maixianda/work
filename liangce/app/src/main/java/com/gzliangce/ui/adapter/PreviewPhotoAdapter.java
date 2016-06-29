package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemPreviewPhotoBinding;
import com.gzliangce.entity.Images;
import com.gzliangce.ui.activity.order.PhotoPreviewActivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by aaron on 16/2/16.
 * 预览图片adapter
 */
public class PreviewPhotoAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private List<Images> imagesList;
    private LayoutInflater layoutInflater;
    private Activity activity;

    public PreviewPhotoAdapter(List<Images> imagesList, Activity activity) {
        this.imagesList = imagesList;
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_preview_photo, null);
        ItemPreviewPhotoBinding binding = ItemPreviewPhotoBinding.bind(view);
        binding.setImages(imagesList.get(position));
        binding.ivPhotoPreview.setOnPhotoTapListener(this);
        container.addView(binding.getRoot());
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((PhotoPreviewActivity) activity).onPhotoClick();
    }
}
