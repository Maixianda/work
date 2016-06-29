package io.ganguo.library.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import io.ganguo.library.BaseApp;
import io.ganguo.library.R;
import io.ganguo.library.util.Colors;

/**
 * UI 辅助工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class UIHelper {

    /**
     * 中止activity运行
     *
     * @param activity
     * @param o
     */
    public static boolean finishActivity(Activity activity, Object o) {
        if (activity != null && o == null) {
            activity.finish();
            return true;
        }
        return false;
    }

    /**
     * 重新启动当前Activity
     *
     * @param activity
     */
    public static void restartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

    /**
     * 绑定返回 自动finish
     *
     * @param activity
     * @param actionBack
     */
    public static void bindActionBack(final Activity activity, final View actionBack) {
        if (activity == null || actionBack == null) {
            return;
        }
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果用到了transition, 请考虑
                activity.finish();
                //activity.finishAfterTransition();
            }
        });
    }

    /**
     * 绑定返回 自动finish
     *
     * @param dialog
     * @param actionBack
     */
    public static void bindActionBack(final Dialog dialog, View actionBack) {
        if (dialog == null || actionBack == null) {
            return;
        }
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示一个snackbar
     * 只适用于{@link android.support.v7.app.AppCompatActivity}
     *
     * @param view
     * @param text
     */
    public static void snackBar(View view, String text) {
        snackBar(view, text, null, null);
    }

    /**
     * 显示一个snackbar
     *
     * @param view
     * @param text
     * @param action
     * @param listener
     */
    public static void snackBar(View view, String text, String action, final View.OnClickListener listener) {
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction(action, listener)
                .setActionTextColor(BaseApp.me().getResources().getColor(R.color.colorAccent));//action颜色
        //文字颜色
        ((TextView) sb.getView().findViewById(R.id.snackbar_text)).setTextColor(Colors.WHITE);
        //背景色
//        snackBarView.setBackgroundColor(colorId);
        sb.show();
    }

}
