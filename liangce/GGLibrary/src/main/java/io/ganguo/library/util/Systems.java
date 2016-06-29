package io.ganguo.library.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.facebook.device.yearclass.YearClass;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.bean.Globals;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 系统/app 工具
 * <p>
 * Created by Tony on 9/30/15.
 */
public class Systems {
    private static final Logger LOG = LoggerFactory.getLogger(Systems.class);

    private Systems() {
        throw new Error(Globals.ERROR_MSG_UTILS_CONSTRUCTOR);
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * dp to px
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        return (int) applyDimension(context, TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    /**
     * 单位转换
     *
     * @param context
     * @param unit    TypedValue.COMPLEX_UNIT_DIP
     * @param value   px
     * @return
     */
    public static float applyDimension(Context context, int unit, float value) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit, value, displayMetrics);
    }

    /**
     * 打开软键盘
     *
     * @param window
     */
    public static void showKeyboard(Window window, View view) {
        if (window.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) window.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (view != null) {
                inputManager.showSoftInput(view, 0);
            }
            inputManager.showSoftInputFromInputMethod(window.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            LOG.e("Sigh, cant even hide keyboard ", e);
        }
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("程序包名无法找到", e);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取当前程序版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取程序版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        String versionName;
        try {
            // Get the package info
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("获取程序版本名称", e);
            return "";
        }
        return versionName;
    }

    /**
     * 获取当前代码版本号
     */
    public static int getVersionCode(Context context) {
        int localVersion = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            localVersion = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("获取当前代码版本号", e);
        }
        return localVersion;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context 上下文
     * @return if application is in background return true, otherwise return
     * false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null
                    && !topActivity.getPackageName().equals(
                    context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件uri
     */
    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 设备级别
     */
    public enum Performance {
        LOW, MEDIUM, HIGH
    }

    /**
     * 设备级别 https://github.com/facebook/device-year-class
     *
     * @return
     */
    public static Performance getPerformance(Context context) {
        int year = YearClass.get(context);
        if (year >= 2014) {
            return Performance.HIGH;
        } else if (year >= 2012) {
            return Performance.MEDIUM;
        } else {
            return Performance.LOW;
        }
    }

    /**
     * 获取设备唯一码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (Strings.isEmpty(deviceId)) {
            deviceId = Config.getString(Globals.KEY_DEVICE_ID);
        }
        if (Strings.isEmpty(deviceId)) {
            deviceId = Strings.randomUUID();
            Config.putString(Globals.KEY_DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 设置状态栏和导航栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setBarTranslucent(Activity activity) {
        setStatusTranslucent(activity);
//        setNavigationTranslucent(activity);
    }

    /**
     * 设置导航栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setNavigationTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setNavigationColor(activity, Color.TRANSPARENT);
    }

    /**
     * 设置状态栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setStatusTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setStatusColor(activity, Color.TRANSPARENT);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(19)
    public static void setBarColor(Activity activity, int color) {
        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        window.setAttributes(winParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
//            window.setNavigationBarColor(color);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(color);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(21)
    public static void setStatusColor(Activity activity, int color) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(21)
    public static void setNavigationColor(Activity activity, int color) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        winParams.flags |= bits;
        window.setAttributes(winParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(color);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(color);
    }

    /**
     * 获取当前窗口的旋转角度
     *
     * @param activity
     * @return
     */
    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 当前是否是横屏
     *
     * @param context
     * @return
     */
    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否是竖屏
     *
     * @param context
     * @return
     */
    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 是否平板设备
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 判断是否需要隐藏键盘
     *
     * @param
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击键盘外区域隐藏键盘
     *
     * @param ev
     * @return
     */
    public static void actionOtherhideKeyboard(Activity activity, MotionEvent ev, View v) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
    }

}
