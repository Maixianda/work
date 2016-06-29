package io.ganguo.library.common;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.ganguo.library.R;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;

/**
 * Toast 辅助类
 * # UI 安全
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class ToastHelper {

    private static Toast toast;

    private static Toast mLocationToast;

    /**
     * 弹出Toast消息
     *
     * @param charSequence
     */
    public static void showMessage(final Context context, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = "";
        }
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, Systems.dpToPx(context, 64));
        toast.show();
    }

    /**
     * 弹出Toast消息
     *
     * @param charSequence
     */
    public static void showMessageMiddle(final Context context, final CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 弹出Toast消息
     *
     * @param resId
     */

    public static void showMessageMiddle(Context context, int resId) {
        showMessageMiddle(context, context.getResources().getText(resId));
    }

    /**
     * 资源ID信息显示
     *
     * @param context
     * @param resId
     */
    public static void showMessage(Context context, int resId) {
        showMessage(context, context.getResources().getText(resId));
    }

    /**
     * 资源ID信息显示
     *
     * @param context
     * @param resId
     * @param duration Toast.LENGTH_SHORT | Toast.LENGTH_LONG
     */
    public static void showMessage(Context context, int resId, int duration) {
        showMessage(context, context.getResources().getText(resId), duration);
    }

    /**
     * 指定消息显示时间
     *
     * @param context
     * @param charSequence
     * @param duration     Toast.LENGTH_SHORT | Toast.LENGTH_LONG
     */
    public static void showMessage(final Context context, final CharSequence charSequence, final int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, charSequence, duration);
        } else {
            toast.setText(charSequence);
        }
        toast.show();
    }


    /**
     * 取消所有toast
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
