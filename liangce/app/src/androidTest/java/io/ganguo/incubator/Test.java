package com.gzliangce;

import android.os.Build;

import io.ganguo.library.util.Systems;

/**
 * Created by Tony on 11/10/15.
 */
public class Test extends ApplicationTest {

    public void test() {
        // app/1.0_dev (android; 4.4.4; 19)
        String userAgent = "app/" + Systems.getVersionName(AppContext.me()) + " (android; " + Build.VERSION.RELEASE + "; " + Build.VERSION.SDK_INT + ")";
        System.out.println(userAgent);
    }

}
