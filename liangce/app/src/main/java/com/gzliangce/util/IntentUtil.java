package com.gzliangce.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gzliangce.AppContext;
import com.gzliangce.bean.Constants;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.order.PlaceAnOrderActivity;
import com.gzliangce.ui.activity.product.ProductsDetailsActivity;
import com.gzliangce.ui.activity.setting.GestureLockActivity;
import com.gzliangce.ui.activity.setting.PinLockActivity;
import com.gzliangce.ui.activity.usercenter.PersonalDetailsActivity;
import com.gzliangce.ui.activity.usercenter.SearchActivity;
import com.leo.gesturelibray.enums.LockMode;

import java.util.List;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/5/17.
 */
public class IntentUtil {
    public static Logger logger = LoggerFactory.getLogger(IntentUtil.class);

    /**
     * webView 加载链接
     */
    public static void actionWebActivity(Context context, String title, int index) {
        List<String> urls = LiangCeUtil.getUrl(context);
        if (urls.size() < 5) {
            ToastHelper.showMessage(context, "服务器内部错误");
            return;
        }
        String url = urls.get(index);
        if (Strings.isEmpty(url)) {
            ToastHelper.showMessage(context, "服务器内部错误");
            return;
        }
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constants.ABOUT_TEXT_TITLE, title);
        intent.putExtra(Constants.ABOUT_TEXT_URL, url);
        context.startActivity(intent);
    }


    /**
     * 跳转到解锁界面
     */
    public static void actionLockActivity(Context context) {
        if (AppContext.isBackGround) {
            return;
        }
        if (!AppContext.me().isLogined()) {
            return;
        }
        String gestLock = Config.getString(AppContext.me().getGestureKey());
        String PinLock = Config.getString(AppContext.me().getPinKey());
        if (Strings.isNotEmpty(gestLock) && !AppManager.isOpenActivity(GestureLockActivity.class)) {
            AppContext.isBackGround = true;
            actionLockActivity(context, LockMode.VERIFY_PASSWORD, GestureLockActivity.class, null);
        } else if (Strings.isNotEmpty(PinLock) && !AppManager.isOpenActivity(PinLockActivity.class)) {
            AppContext.isBackGround = true;
            actionLockActivity(context, LockMode.VERIFY_PASSWORD, PinLockActivity.class, null);
        }
    }


    /**
     * 跳转到解锁界面
     *
     * @param unLockJumActivity 解锁成功后需要打开得页面，不需要打开传null
     * @param mClass            需要跳转的界面
     * @param mode              设置密码的模式
     */
    public static void actionLockActivity(Context context, LockMode mode, Class mClass, Class unLockJumActivity) {
        Intent intent = new Intent(context, mClass);
        if (unLockJumActivity != null) {
            intent.putExtra(Constants.IS_UNLOCK_JUM_ACTIVITY_NAME, unLockJumActivity);
        }
        intent.putExtra(Constants.IS_GESTURE_LOCK_MODE, mode);
        context.startActivity(intent);
    }


    /**
     * 当手势密码开启时，再开启PIN码需要验证手势，关闭PIN码成功后，跳转到PIN密码设置界面
     * 当PIN码开启时，再开启手势解锁也一样
     */
    public static void actionLockStingActivity(Activity activity, Class unLuckJumActivity) {
        if (unLuckJumActivity != null) {
            Intent intent = new Intent(activity, unLuckJumActivity);
            intent.putExtra(Constants.IS_GESTURE_LOCK_MODE, LockMode.SETTING_PASSWORD);
            activity.startActivity(intent);
        }
        activity.finish();
    }


    /**
     * 跳转到登录界面，登录成功后跳转到主界面，否则finish
     */
    public static void actionLoginActivity(Activity context) {
        Config.remove(AppContext.me().getPinKey());
        Config.remove(AppContext.me().getGestureKey());
        AppContext.me().setUser(null);
        AVImClientManagerUtil.getInstance().doColseClient();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Constants.IS_ACTION_MANIN_ACTIVITY, true);
        context.startActivity(intent);
        context.finish();
    }


    /**
     * 跳转到经纪人详情
     *
     * @param info         按揭员信息
     * @param buttonStatus 按钮显示状态
     * @param placeAnOrder 下单相关信息
     */
    public static void actionPersonalDetailsActivity(BrokeInfo info, PlaceAnOrder placeAnOrder, ButtonStatus buttonStatus) {
        logger.e("AppManager:" + AppManager.getLastsOpenActivity().getClass().getName());
        Intent intent = new Intent(AppManager.getLastsOpenActivity(), PersonalDetailsActivity.class);
        intent.putExtra(Constants.REQUEST_BROKE_INFO_ID, info.getAccountId());
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, buttonStatus);
        AppManager.getLastsOpenActivity().startActivity(intent);
    }


    /**
     * 跳转到搜索页面
     */
    public static void intentSearchActivity(Activity activity, int intentType, PlaceAnOrder placeAnOrder, ButtonStatus buttonStatus) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_RESULT_TYPE, intentType);
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, buttonStatus);//搜索金融经纪时需要用到
        activity.startActivity(intent);
    }


    /**
     * 跳转到产品详情页
     */
    public static void actionProductsActivity(Activity activity, ProductsInfo productsInfo) {
        Intent intent = new Intent(activity, ProductsDetailsActivity.class);
        intent.putExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY, productsInfo);
        activity.startActivity(intent);
    }

    /**
     * 跳转到文章详情页面
     */
    public static void actionNewSDetaiActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constants.ABOUT_TEXT_TITLE, title);
        intent.putExtra(Constants.ABOUT_TEXT_URL, url);
        context.startActivity(intent);
    }


    /**
     * 直接下单按钮跳转
     */
    public static void actionDirectOrder(Context context) {
        Intent intent = new Intent(context, PlaceAnOrderActivity.class);
        intent.putExtra(Constants.IS_DIRECT, true);
        context.startActivity(intent);
    }


    /**
     * 通用跳转方法
     */
    public static void actionActivity(Context context, Class mClass) {
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
    }
}
