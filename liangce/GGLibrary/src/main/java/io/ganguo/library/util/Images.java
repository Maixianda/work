package io.ganguo.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.ganguo.library.bean.Globals;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 图片处理工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Images {
    public final static Logger logger = LoggerFactory.getLogger(Images.class);

    private Images() {
        throw new Error(Globals.ERROR_MSG_UTILS_CONSTRUCTOR);
    }

    /**
     * 保存为PNG
     * (完成后回收bitmap)
     *
     * @param bitmap
     * @param to
     * @throws IOException
     */
    public static void savePNG(Bitmap bitmap, int quality, File to) throws IOException {
        OutputStream out = new FileOutputStream(to);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
        } finally {
            out.close();
            recycle(bitmap);
        }
    }

    /**
     * 保存为JPEG
     * (完成后回收bitmap)
     *
     * @param bitmap
     * @param to
     * @throws IOException
     */
    public static void saveJPEG(Bitmap bitmap, int quality, File to) throws IOException {
        OutputStream out = new FileOutputStream(to);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } finally {
            out.close();
            recycle(bitmap);
        }
    }

    /**
     * 压缩图片文件到指定大小
     * (完成后回收bitmap)
     * TODO 添加支持Exif
     *
     * @param from
     * @param limitKB (kb)
     * @param to
     * @throws IOException
     */
    public static void compress(final File from, final int limitKB, final File to) throws IOException {
        //这步容易oom,图片较大的话建议先缩小图片长宽
        Bitmap bitmap = BitmapFactory.decodeFile(from.getAbsolutePath());
        compress(bitmap, limitKB, to);
    }

    /**
     * 压缩bitmap到指定大小
     * (完成后回收bitmap)
     * TODO 添加支持Exif
     *
     * @param bitmap
     * @param limitKB (kb)
     * @param to
     * @throws IOException
     */
    public static void compress(final Bitmap bitmap, final int limitKB, final File to) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // scale
            int quality = 100;
            int interval = 10;
            // Store the bitmap into output stream(no compress)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            // Compress by loop
            while (os.toByteArray().length / 1024 > limitKB && quality > interval) {
                // Clean up os
                os.reset();
                // interval 10
                quality -= interval;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            }
            // Generate compressed image file
            FileOutputStream fos = new FileOutputStream(to);
            try {
                fos.write(os.toByteArray());
                fos.flush();
            } finally {
                fos.close();
            }
        } finally {
            os.close();

            recycle(bitmap);
        }
    }

    /**
     * (建议异步执行本方法)
     * 读取sd card 上的图片(强烈拒绝oom)
     *
     * @param filePath  图片路径
     * @param reqWidth  理想的目标宽度, 实际显示的图片会>=目标宽度, 可以为0(读取原图大小)
     * @param reqHeight 理想的目标高度, 实际显示的图片会>=目标高度, 可以为0(读取原图大小)
     * @return
     */
    public static Bitmap decodeFile(String filePath, int reqWidth, int reqHeight) {
        if (!Files.checkFileExists(filePath)) {
            logger.w("decodeFile failed, empty image.");
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int be = sampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        while (true) {
            try {
                //设置缩放比例
                options.inSampleSize = be;
                logger.d("filePath:" + filePath + ", be:" + be + ", oriWidth:" + options.outWidth + ", oriHeight:" + options.outHeight);
                return BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError outOfMemoryError) {
                logger.e("java.lang.OutOfMemoryError,current be:" + be);
                be++;
            }
        }
    }

    /**
     * 计算图片尺寸的压缩比
     * 用法{@link Images#decodeFile}
     *
     * @param reqWidth  理想的目标宽度, 实际显示的图片会>=目标宽度, 可以为0
     * @param reqHeight 理想的目标高度, 实际显示的图片会>=目标高度, 可以为0
     */
    public static int sampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {
        // 源图片的高度和宽度
        final int h = options.outHeight;
        final int w = options.outWidth;

        int be = 1;
        if (reqWidth > 0 && reqHeight > 0) {
            //宽高都有限制
            final int heightRatio = calcBe(h, reqHeight);
            final int widthRatio = calcBe(w, reqWidth);
            be = heightRatio < widthRatio ? heightRatio : widthRatio;
        } else if (reqWidth > 0) {
            //只有宽度限制
            be = calcBe(w, reqWidth);
        } else if (reqHeight > 0) {
            //只有高度限制
            be = calcBe(h, reqHeight);
        }

        return be;
    }

    /**
     * 图片尺寸的压缩比, 2的n次幂
     * {@link BitmapFactory.Options#inSampleSize}
     *
     * @param oriPx 原图中的单边size
     * @param reqPx 目标的单边size
     * @return
     */
    private static int calcBe(final float oriPx, final float reqPx) {
        if (oriPx > reqPx) {
            float ratio = oriPx / reqPx;
            int n = 1;
            while ((n * 2) <= ratio) {
                n *= 2;
            }
            return n;
        }
        return 1;
    }

    /**
     * 获取bitmap占用的内存体积(byte)
     *
     * @param bitmap byte
     * @return
     */
    public static long byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return (long) bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    /**
     * 获取图片中的缩略图 JPEG Exif
     *
     * @param path
     * @return
     */
    public static Bitmap getThumbnail(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        byte[] bytes = exif.getThumbnail();
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * View to Bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap toBitmap(View view) {
        if (view == null) {
            return null;
        }
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        // 如果已经带有宽度和高度，那直接拿出来使用，就不再进行计算，加快很多
        int width = -1;
        int height = -1;
        if (view.getLayoutParams() != null) {
            width = view.getLayoutParams().width;
            height = view.getLayoutParams().height;
        }
        if (width > 0 && height > 0) {
            // 已有宽高
            view.measure(width, height);
        } else {
            // 计算宽高
            view.measure(View.MeasureSpec.makeMeasureSpec(
                            0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
        }
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable) {
        // is BitmapDrawable
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // draw to canvas
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degrees
     * @return bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        // bitmap new
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true
        );
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int getImageDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            logger.e("failed to read degree:", e);
        }
        return degree;
    }


    /**
     * 从asset中读取bitmap  Warn:耗时操作
     *
     * @param context
     * @param name    相对文件名, 支持多层结构, 如"cover.png", "images/pro/11/detail.png",
     * @return bitmap
     * @throws IOException
     */
    public static Bitmap getAssetsAsBitmap(Context context, String name) throws IOException {
        InputStream in = context.getAssets().open(name);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(in);
        } finally {
            in.close();
        }
        return bitmap;
    }

    /**
     * recycle bitmap
     * <p/>
     * TODO 如果全局使用glide的话可以考虑bitmap-pool回收
     *
     * @param bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * recycle drawable
     * -> recycle(Bitmap bitmap)
     *
     * @param drawable
     */
    public static void recycle(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof BitmapDrawable) {
            recycle(((BitmapDrawable) drawable).getBitmap());
        }
    }

    /**
     * recycle imageView
     * -> recycle(Bitmap bitmap)
     *
     * @param imageView
     */
    public static void recycle(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        Drawable drawable = imageView.getDrawable();
        imageView.setImageBitmap(null);
        imageView.setImageDrawable(null);
        recycle(drawable);
    }

    /**
     * 毛玻璃效果
     *
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
