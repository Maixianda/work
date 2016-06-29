package com.gzliangce.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.qualification.QualificationActivity;

import java.util.ArrayList;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Exits;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

import static com.afollestad.materialdialogs.MaterialDialog.Builder;
import static com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;

/**
 * Created by leo on 16/1/23.
 * 对话框工具类
 */
public class DialogUtil {
    private static Logger logger = LoggerFactory.getLogger(DialogUtil.class);
    public static ArrayList<String> houseTypeData = null;//房屋类型
    public static ArrayList<String> mortgageNumData = null;
    public static ArrayList<String> houseYearData = null;//房屋年限数据
    private static MaterialDialog restartDialog = null;//多处登录，提示dialog


    /**
     * 退出app
     */
    public static void exitAppDialog(Activity activity) {
        getMaterialDialog(activity, activity.getResources().getString(R.string.exit_text), new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Exits.exit();
            }
        }).show();

    }


    /**
     * 资格认证Dialog
     */
    public static void AtterstationDialog(final Activity activity, String msg) {
        getMaterialDialog(activity, msg, new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent(activity, QualificationActivity.class);
                activity.startActivity(intent);
            }
        }).show();
    }

    /**
     * 退出app对话框
     */
    public static void exitByDialog(final Activity activity) {
        exitAppDialog(activity);
    }

    /**
     * 提示dialog
     */
    public static void showHintDialog(final Activity activity, String text) {
        showHintDialog(activity, text, new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 密码输入次数过多dialog
     */
    public static void showErrorPasswordDialog(final Activity activity) {
        showHintDialog(activity, activity.getResources().getText(R.string.error_count_text), new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                IntentUtil.actionLoginActivity(activity);
            }
        }).show();
    }


    /**
     * 复用的选择器
     */
    public static OptionsPickerView showPickerView(Context context, ArrayList<String> data, int options, OptionsPickerView.OnOptionsSelectListener selectListener) {
        Systems.hideKeyboard(context);
        //选项选择器
        OptionsPickerView productOptions = new OptionsPickerView(context);
        //三级联动效果
        productOptions.setPicker(data, null, null, true);
        productOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        productOptions.setSelectOptions(options);
        productOptions.setOnoptionsSelectListener(selectListener);
        productOptions.show();
        return productOptions;
    }


    /**
     * 房屋类型数据
     */
    public static ArrayList<String> getHouseTypeData() {
        if (houseTypeData == null) {
            houseTypeData = new ArrayList<>();
            houseTypeData.add("普通住宅");
            houseTypeData.add("非普通住宅");
            houseTypeData.add("经济适用房");
        }
        return houseTypeData;
    }


    /**
     * 获取房屋年限
     */
    public static ArrayList<String> getHouseYearData() {
        if (houseYearData == null) {
            houseYearData = new ArrayList<>();
            houseYearData.add("不满2年");
            houseYearData.add("满2年不满5年");
            houseYearData.add("满5年");
        }
        return houseYearData;
    }


    /**
     * 按揭期数数据
     */
    public static ArrayList<String> getMortgageNmuData() {
        if (mortgageNumData == null) {
            mortgageNumData = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                mortgageNumData.add(String.format(AppContext.me().getResources().getString(R.string.mortgage_num_text), i + 1, (i + 1) * 12));
            }
        }
        return mortgageNumData;
    }


    /**
     * 设置Dialog位置的style(该方法适用于不需要点击dialog外部隐藏的情况)
     */
    public static void setDialogLayoutParams(BaseDialog dialog, int resId) {
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setWindowAnimations(resId);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }


    /**
     * 显示重新登录对话框
     */
    public static MaterialDialog showLogoutDialog() {
        Activity activity = AppManager.getLastsOpenActivity();
        if (restartDialog == null) {
            restartDialog = showHintDialog(activity, activity.getResources().getString(R.string.restart_text), new SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    restartLogin();
                }
            });
        }
        if (activity.getClass() != LoginActivity.class && !restartDialog.isShowing()) {
            AppContext.me().setUser(null);
            if (restartDialog.getContext() != null) {
                restartDialog.show();
            }
        }
        return restartDialog;
    }

    /**
     * 重新登录
     */
    private static void restartLogin() {
        if (AppManager.isOpenActivity(LoginActivity.class)) {
            return;
        }
        IntentUtil.actionLoginActivity(AppManager.getLastsOpenActivity());
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppManager.finishOtherActivity(AppManager.getLastsOpenActivity(), AppManager.getFristOpenActivity());
            }
        }, 2000);
        //需要重新登录的处理方法放这里
        logger.e("token与服务器不一致需要重新登录");
    }


    /**
     * 延时隐藏Loading
     */
    public static void hideLoading() {
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingHelper.hideMaterLoading();
            }
        }, 500);
    }


    /**
     * MaterialDialog 普通提示Dialog
     *
     * @param activity
     * @param message
     * @param callback
     */
    public static MaterialDialog getMaterialDialog(final Activity activity, String message, SingleButtonCallback callback) {
        MaterialDialog dialog = new Builder(activity)
                .title("提示")
                .cancelable(false)
                .content(message)
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeColorRes(R.color.gray_6e)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(callback)
                .build();
        return dialog;
    }


    /**
     * MaterialDialog 单个按钮Dialog
     *
     * @param activity
     * @param message
     * @param callback
     */
    public static MaterialDialog showHintDialog(final Activity activity, CharSequence message, SingleButtonCallback callback) {
        MaterialDialog dialog = new Builder(activity)
                .title("提示")
                .cancelable(false)
                .content(message)
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeColorRes(R.color.gray_6e)
                .positiveText("确认")
                .onPositive(callback)
                .build();
        return dialog;
    }


    /**
     * MaterialDialog 注销账号Dialog
     *
     * @param activity
     */
    public static void LogOutDialog(final Activity activity) {
        DialogUtil.getMaterialDialog(activity, activity.getResources().getString(R.string.log_out_text), new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                IntentUtil.actionLoginActivity(activity);
            }
        }).show();
    }

    /**
     * 确认拨打电话Dialog
     */
    public static void showCallDialog(final Activity activity, final String phone) {
        DialogUtil.getMaterialDialog(activity, activity.getResources().getString(R.string.call_text), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                actionCall(activity, phone.replace("-", ""));
            }
        }).show();
    }


    /**
     * 拨打客服电话
     */
    public static void actionCall(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}
