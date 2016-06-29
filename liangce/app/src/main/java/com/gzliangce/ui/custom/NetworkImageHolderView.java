package com.gzliangce.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.gzliangce.R;
import com.gzliangce.entity.BannerImageInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import io.ganguo.library.core.image.PhotoLoader;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<BannerImageInfo> {
    private ImageView imageView;
    private Activity activity;

    public NetworkImageHolderView(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setEnabled(false);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerImageInfo data) {
        PhotoLoader.display(activity, imageView, data.getImage(), activity.getResources().getDrawable(R.drawable.shape_image_loading));
    }
}
