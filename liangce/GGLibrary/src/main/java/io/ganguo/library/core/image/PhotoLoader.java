package io.ganguo.library.core.image;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

/**
 * 图片加载工具（Glide）
 * Created by hulk on 25/12/2015.
 */
public class PhotoLoader {

    /**
     * 加载uri格式的图片
     *
     * @param context
     * @param def
     * @param uri
     * @param imageView
     */
    public static void display(Context context, int def, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .placeholder(def)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//default
                .into(imageView);

    }

    /**
     * 加载string格式的图片
     *
     * @param context
     * @param src
     * @param url
     * @param imageView
     */
    public static void display(Context context, ImageView imageView, String url, Drawable src) {
        Glide.with(context)
                .load(url)
                .placeholder(src)
                .error(src)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//default
                .into(imageView);
    }

    @BindingAdapter(value = {"glide:url", "android:src"}, requireAll = false)
    public static void display(ImageView view, String url, Drawable src) {
        display(view.getContext(), view, url, src);
    }

    /**
     * 加载res图片
     *
     * @param context
     * @param def
     * @param resourceId
     * @param imageView
     */
    public static void display(Context context, int def, int resourceId, ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .placeholder(def)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//default
                .into(imageView);
    }

    /**
     * 加载本地file
     *
     * @param imageView
     * @param file
     */
    @BindingAdapter(value = {"glide:file", "android:src"}, requireAll = false)
    public static void display(ImageView imageView, File file, Drawable src) {
        Glide.with(imageView.getContext())
                .load(file)
                .placeholder(src)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//default
                .into(imageView);
    }

    /**
     * 加载图片并显示成圆形
     *
     * @param context
     * @param src
     * @param url
     * @param imageView
     */
    public static void displayRoundImage(final Context context, final ImageView imageView, String url, Drawable src) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(src)
                .error(src)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }

    public static void displayRoundImage(final Context context, final ImageView imageView, int resId) {
        Glide.with(context)
                .load(resId)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }

    @BindingAdapter(value = {"glide:roundUrl", "android:src"}, requireAll = false)
    public static void displayRoundImage(ImageView view, String roundUrl, Drawable src) {
        displayRoundImage(view.getContext(), view, roundUrl, src);
    }

    /**
     * 加载缩略图
     *
     * @param context
     * @param def
     * @param url
     * @param imageView
     */
    public static void displayThumbImage(Context context, int def, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .thumbnail(0.2f)
                .crossFade()
                .placeholder(def)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
