package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemPreviewImageBinding;
import com.gzliangce.entity.QualificationImageInfo;
import com.gzliangce.ui.callback.IonClickCallback;

import java.util.List;

import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.util.Strings;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by leo on 16/1/15.
 * 预览图片adapter
 */
public class PreviewImageAdapter extends PagerAdapter implements PhotoViewAttacher.OnViewTapListener {

    private List<QualificationImageInfo> qualificationImageInfos;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private IonClickCallback ionClickCallback;

    public PreviewImageAdapter(List<QualificationImageInfo> bannerImageInfoList, Activity activity, IonClickCallback ionClickCallback) {
        this.qualificationImageInfos = bannerImageInfoList;
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.ionClickCallback = ionClickCallback;
    }

    @Override
    public int getCount() {
        return qualificationImageInfos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_preview_image, null);
        ItemPreviewImageBinding binding = ItemPreviewImageBinding.bind(view);
        initData(binding.ivPreview, qualificationImageInfos.get(position));
        binding.ivPreview.setOnViewTapListener(this);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    /**
     * 加载图片数据
     */
    private void initData(ImageView item_iv_banner, QualificationImageInfo info) {
        if (info == null) {
            return;
        }
        if (Strings.isNotEmpty(info.getImageUrl())) {
            PhotoLoader.display(item_iv_banner, info.getImageUrl(), activity.getResources().getDrawable(R.drawable.shape_image_loading));
        } else {
            PhotoLoader.display(item_iv_banner, info.getFile(), activity.getResources().getDrawable(R.drawable.shape_image_loading));
        }
    }


    @Override
    public void onViewTap(View view, float x, float y) {
        ionClickCallback.onClick(view);
    }
}
