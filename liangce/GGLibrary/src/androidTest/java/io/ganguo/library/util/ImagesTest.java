package io.ganguo.library.util;

import android.graphics.Bitmap;

import java.io.File;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Wilson on 14/12/15.
 */
public class ImagesTest extends ApplicationTest {
    /**
     * 先定义具体的照片
     */
    public void testDecodeFile() {
        //test via xiaomi3
        File dcim = Files.getStorageDirectory(getContext(), "DCIM");
        String b_hsq01 = dcim.getAbsolutePath() + File.separator + "hsq" + File.separator + "b_hsq01.jpg";
        //实际使用的时候强烈建议异步处理Bitmap
        decode(b_hsq01, 100, 100);
        decode(b_hsq01, 100, 0);
        decode(b_hsq01, 0, 100);
        decode(b_hsq01, Systems.getScreenWidth(getContext()), 0);
    }

    private void decode(String path, int reqW, int reqH) {
        long start = System.currentTimeMillis();
        Bitmap bitmap2 = Images.decodeFile(path, reqW, reqH);
        logger.d("end_b_hsq01, width:" + bitmap2.getWidth() + ", height:" + bitmap2.getHeight()
                        + ", sizeMbInRam:" + (Images.byteSizeOf(bitmap2) / 1024f / 1024f)
                        + ", reqW:" + reqW
                        + ", reqH:" + reqH
                        + ", time:" + (System.currentTimeMillis() - start)
        );
    }
}
