package com.gzliangce.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.dto.MetadataDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.AccountStatusInfo;
import com.gzliangce.entity.AreaList;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.AttestationType;
import com.gzliangce.enums.DocumentType;
import com.gzliangce.enums.OrderStatusType;
import com.gzliangce.enums.ProductType;
import com.gzliangce.enums.UserType;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.product.ProductsDetailsActivity;
import com.gzliangce.ui.activity.setting.GestureLockActivity;
import com.gzliangce.ui.activity.setting.PinLockActivity;
import com.gzliangce.ui.activity.usercenter.SearchActivity;
import com.leo.gesturelibray.enums.LockMode;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.date.BaseDate;
import io.ganguo.library.util.date.DateTime;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;


/**
 * Created by leo on 16/1/6.
 * 工具类
 */
public class LiangCeUtil {
    private static Logger logger = LoggerFactory.getLogger(LiangCeUtil.class);
    private static Toast mCenterToast;
    private static TextView tv_center_hint;
    private static int color[] = {R.color.blue_00a, R.color.orange_f8, R.color.green_7e};

    private ArrayList<String> secondStageCity = new ArrayList<String>();
    private ArrayList<ArrayList<String>> secondStageRegion = new ArrayList<ArrayList<String>>();
    private ArrayList<String> secondStageCityId = new ArrayList<String>();
    private ArrayList<ArrayList<String>> secondStageRegionId = new ArrayList<ArrayList<String>>();
    private ArrayList<String> thirdStageRegion = new ArrayList<String>();
    private ArrayList<String> thirdStageRegionId = new ArrayList<String>();

    private ArrayList<String> province = new ArrayList<String>();
    private ArrayList<ArrayList<String>> city = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> region = new ArrayList<ArrayList<ArrayList<String>>>();

    private ArrayList<String> provinceId = new ArrayList<String>();
    private ArrayList<ArrayList<String>> cityId = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> regionId = new ArrayList<ArrayList<ArrayList<String>>>();

    /**
     * 首页切换fragment，icon更换
     *
     * @param position
     * @param imageViewPosition
     * @return
     */
    public static int setImgbg(int position, int imageViewPosition) {
        if (position == imageViewPosition) {
            switch (position) {
                case 1:
                    return R.drawable.ic_main_news_selected;
                case 2:
                    return R.drawable.ic_main_college_selected;
                case 3:
                    return R.drawable.ic_main_order_selected;
                case 4:
                    return R.drawable.ic_main_message_selected;
                case 5:
                    return R.drawable.ic_main_mine_selected;
            }
        } else {
            switch (imageViewPosition) {
                case 1:
                    return R.drawable.ic_main_news;
                case 2:
                    return R.drawable.ic_main_college;
                case 3:
                    return R.drawable.ic_main_order;
                case 4:
                    return R.drawable.ic_main_message;
                case 5:
                    return R.drawable.ic_main_mine;
            }
        }
        return 0;
    }

    /**
     * 首页切换fragment，text更换颜色
     *
     * @param position
     * @param imageViewPosition
     * @return
     */
    public static int setTextColor(int position, int imageViewPosition) {
        if (position == imageViewPosition) {
            switch (position) {
                case 1:
                    return 0xfffa4141;
                case 2:
                    return 0xfffa4141;
                case 3:
                    return 0xfffa4141;
                case 4:
                    return 0xfffa4141;
                case 5:
                    return 0xfffa4141;
            }
        } else {
            switch (imageViewPosition) {
                case 1:
                    return 0xff959595;
                case 2:
                    return 0xff959595;
                case 3:
                    return 0xff959595;
                case 4:
                    return 0xff959595;
                case 5:
                    return 0xff959595;
            }
        }
        return 0;
    }


    public static String displayText(AccountStatusInfo userInfo) {
        if (userInfo != null) {
            if (userInfo.isLanguagePage()) {
                return userInfo.getLanguage();
            } else {
                return userInfo.getFunctions();
            }
        }
        return "";
    }


    /**
     * 根据字符编码计算文字长度
     */
    public static String getLimitSubstring(String inputStr, int length, TextView textView) {
        int orignLen = inputStr.length();
        int resultLen = 0;
        String temp = null;
        for (int i = 0; i < orignLen; i++) {
            temp = inputStr.substring(i, i + 1);
            try {
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int inNum = length - resultLen / 2;
            textView.setText(inNum + "");
            if (resultLen / 2 > length) {
                return inputStr.substring(0, i);
            }
        }
        return inputStr;
    }


    /**
     * 我的订单，根据不同订单类型更换icon
     *
     * @param orderType
     * @return
     */
    public static int setOrderItemIcon(int orderType) {
        switch (orderType) {
            case 1:
                //按揭
                return R.drawable.ic_mortgage;
            case 2:
                //金融
                return R.drawable.ic_finance;
            case 3:
                //分期
                return R.drawable.ic_stage;
        }
        return R.drawable.ic_mortgage;
    }


    /**
     * 弹出有图片的toast
     */
    public static void showCenterToast(Activity context, String hint, boolean isBottom) {
        if (mCenterToast == null) {
            mCenterToast = new Toast(context);
            mCenterToast.setDuration(Toast.LENGTH_SHORT);
            View toastView = context.getLayoutInflater().inflate(R.layout.item_image_toast, null);
            tv_center_hint = (TextView) toastView.findViewById(R.id.tv_hint);
            mCenterToast.setView(toastView);
        }
        if (isBottom) {
            mCenterToast.setGravity(Gravity.BOTTOM, 0, context.getResources().getDimensionPixelSize(R.dimen.dp_17));
        } else {
            mCenterToast.setGravity(Gravity.CENTER, 0, 0);
        }
        if (tv_center_hint != null) {
            tv_center_hint.setText(hint);
        }
        mCenterToast.show();
    }


    /**
     * 获取认证状态图标
     */
    public static Drawable getAttestationDrable(String status) {
        Drawable drawable = null;
        if (Strings.isEquals(status, AttestationType.pass.toString())) {
            drawable = AppContext.me().getResources().getDrawable(R.drawable.ic_pass);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        } else if (Strings.isEquals(status, AttestationType.nopass.toString())) {
            drawable = AppContext.me().getResources().getDrawable(R.drawable.ic_no_pass);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        } else if (Strings.isEquals(status, AttestationType.noauth.toString())) {
            drawable = AppContext.me().getResources().getDrawable(R.drawable.ic_tick);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        } else {
            drawable = AppContext.me().getResources().getDrawable(R.drawable.ic_arrow);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        return drawable;
    }

    /**
     * 获取认证文字颜色
     */
    public static int getAttestationTextColor(String status) {
        if (Strings.isEquals(status, AttestationType.pass.toString())) {
            return AppContext.me().getResources().getColor(R.color.blue_33);
        }
        return AppContext.me().getResources().getColor(R.color.gray_95);
    }


    /**
     * 获取认证文字颜色
     */
    public static String getAttestationTextStr(String status) {
        if (Strings.isEquals(status, AttestationType.pass.toString())) {
            return "已通过资格认证";
        }
        if (Strings.isEquals(status, AttestationType.noauth.toString())) {
            return "未认证";
        }
        if (Strings.isEquals(status, AttestationType.check.toString())) {
            return "审核中";
        }
        return "认证不通过";
    }


    /**
     * 保留两位小数
     */
    public static int getPrecisionData(float ft) {
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal((double) ft);
        bd = bd.setScale(scale, roundingMode);
        return (int) (bd.floatValue() * 100);
    }


    /**
     * 获取登录用户的类型
     */
    public static UserType getUserType() {
        if (!AppContext.me().isLogined()) {
            return UserType.mediator;
        }
        String userType = AppContext.me().getUser().getType();
        if (Strings.isEquals(userType, UserType.mortgage.toString())) {
            return UserType.mortgage;
        }
        if (Strings.isEquals(userType, UserType.simpleUser.toString())) {
            return UserType.simpleUser;
        }
        return UserType.mediator;
    }

    /**
     * 筛选物业区域数据
     *
     * @param firstAreaList
     */
    public void FilterData(AreaList firstAreaList) {
        if (firstAreaList.getChildren() != null && firstAreaList.getChildren().size() > 0) {
            for (AreaList seacondAreaList : firstAreaList.getChildren()) {
                thirdStageRegion = new ArrayList<String>();
                thirdStageRegionId = new ArrayList<String>();
                if (seacondAreaList.getChildren() != null && seacondAreaList.getChildren().size() > 0) {
                    for (AreaList thirdAreaList : seacondAreaList.getChildren()) {
                        thirdStageRegion.add(thirdAreaList.getName());
                        thirdStageRegionId.add(thirdAreaList.getAreaId() + "");
                    }
                } else {
                    thirdStageRegion.add("");
                    thirdStageRegionId.add("");
                }
                secondStageCity.add(seacondAreaList.getName());
                secondStageRegion.add(thirdStageRegion);

                secondStageCityId.add(seacondAreaList.getAreaId() + "");
                secondStageRegionId.add(thirdStageRegionId);
            }
        } else {
            secondStageCity.add("");
            ArrayList<String> thirdStageRegion = new ArrayList<String>();
            secondStageRegion.add(thirdStageRegion);
            secondStageCityId.add("");
            secondStageRegionId.add(thirdStageRegion);
        }

        province.add(firstAreaList.getName());
        city.add(secondStageCity);
        region.add(secondStageRegion);

        provinceId.add(firstAreaList.getAreaId() + "");
        cityId.add(secondStageCityId);
        regionId.add(secondStageRegionId);
    }


    /**
     * 转换时间戳为字符串
     *
     * @param timestamp
     * @return
     */
    public static String showCreateTime(long timestamp) {
        if (timestamp == 0) {
            return "";
        }
        BaseDate date = new BaseDate(timestamp);
        DateTime.formatFor(date);
        return DateTime.formatFor(date);
    }

    /**
     * 转换时间戳为字符串
     *
     * @param timestamp
     * @return
     */
    public static String showCreateTime(String timestamp) {
        if (Strings.isEmpty(timestamp) || Strings.isEquals(timestamp, "0")) {
            return "";
        }
        BaseDate date = new BaseDate(Long.parseLong(timestamp));
        return DateTime.formatLiangCeFor(date);
    }

    /**
     * 判断是否显示进度查询条件
     *
     * @param timestamp
     * @return
     */
    public static boolean displayProgress(String timestamp) {
        if (Strings.isEmpty(timestamp) || Strings.isEquals(timestamp, "0")) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户类型
     *
     * @param userType
     * @return
     */
    public static boolean judgeUserType(UserType userType) {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        AccountInfo info = AppContext.me().getUser();
        if (Strings.isEquals(info.getType(), userType.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断订单状态类型
     *
     * @param userType
     * @return
     */
    public static boolean judgeOrderStatus(String status, OrderStatusType orderStatusType, UserType userType) {
        if (judgeUserType(userType)) {
            if (Strings.isEquals(status, orderStatusType.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断订单状态显示电话
     *
     * @param status
     * @return
     */
    public static boolean showphone(String status) {
        if (LiangCeUtil.getUserType() != UserType.simpleUser) {
            return true;
        }
        if (Strings.isEquals(status, OrderStatusType.receive.toString())) {
            return true;
        }
        if (Strings.isEquals(status, OrderStatusType.signed.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户跟订单状态显示操作的按钮(接单，下单)
     *
     * @param status
     * @return
     */
    public static boolean showPlaceAnOrderBtn(String status, boolean evaluate) {
        if (judgeUserType(UserType.mortgage)) {
            if (Strings.isEquals(status, OrderStatusType.wait.toString())) {
                return true;
            }
        }

        if (judgeUserType(UserType.mediator)) {
            if (Strings.isEquals(status, OrderStatusType.wait.toString())) {
                return true;
            }
            if (Strings.isEquals(status, OrderStatusType.signed.toString())) {
                if (evaluate) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 判断订单状态显示案件详情
     *
     * @param status
     * @return
     */
    public static boolean showCaseDetail(String status) {
        if (judgeUserType(UserType.mortgage)) {
            if (Strings.isEquals(status, OrderStatusType.signed.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户跟订单状态显示操作的按钮(接单，下单)
     *
     * @param status
     * @return
     */
    public static boolean showOperationBtn(String status) {
        if (judgeUserType(UserType.mortgage)) {
            if (Strings.isEquals(status, OrderStatusType.wait.toString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断用户跟订单状态显示操作的按钮(接单，下单)
     *
     * @param status
     * @return
     */
    public static boolean showOperationBtn(String status, boolean isCanTransfer) {
        if (judgeUserType(UserType.mortgage)) {
            if (Strings.isEquals(status, OrderStatusType.wait.toString()) && isCanTransfer) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户,来显示text
     *
     * @return
     */
    public static String showBtnText(String status) {
        if (judgeUserType(UserType.mediator)) {
            if (Strings.isEquals(status, OrderStatusType.wait.toString())) {
                return "取消订单";
            }
            if (Strings.isEquals(status, OrderStatusType.signed.toString())) {
                return "评价订单";
            }
        }
        return "我要接单";
    }


    /**
     * 按揭判断订单类型显示签约拍照按钮
     *
     * @return
     */
    public static boolean displaySignPic(String status, OrderStatusType orderStatusType) {
        if (judgeOrderStatus(status, orderStatusType, UserType.mortgage)) {
            return true;
        }
        return false;
    }

    /**
     * 按揭判断订单类型显示进度查询按钮
     *
     * @return
     */
    public static boolean displaypProgressBtn(String status) {
        if (Strings.isEquals(status, OrderStatusType.receive.toString())) {
            return true;
        }
        if (Strings.isEquals(status, OrderStatusType.signed.toString())) {
            return true;
        }
        return false;
    }

    public static String showPhotoTitle(String label) {
        if (Strings.isEquals(label, "a")) {
            return "与买家同照";
        } else if (Strings.isEquals(label, "b")) {
            return "与业主同照";
        } else if (Strings.isEquals(label, "c")) {
            return "与XX同照";
        } else if (Strings.isEquals(label, "d")) {
            return "与XX同照";
        } else if (Strings.isEquals(label, "e")) {
            return "与XX同照";
        }
        return "";
    }

    /**
     * 根据文档资料type现实title
     */
    public static String showDocumentTille(String documentType) {
        if (Strings.isEquals(documentType, DocumentType.contract.toString())) {
            return "合同";
        }
        if (Strings.isEquals(documentType, DocumentType.bankDetail.toString())) {
            return "银行明细";
        }
        if (Strings.isEquals(documentType, DocumentType.loanDetail.toString())) {
            return "贷款明细";
        }
        return "";
    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 按钮text显示（下单/转单）
     *
     * @return
     */
    public static String showMapBtnText() {
        if (judgeUserType(UserType.mortgage)) {
            return "转单";
        }
        return "下单";
    }


    /**
     * 获取解锁Runnable线程
     */
    public static Runnable getLockRunnable(final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                IntentUtil.actionLockActivity(context);
            }
        };
        return runnable;
    }


    /**
     * 判断用户ID是否想同
     *
     * @param userId
     * @return
     */
    public static boolean judgeUserId(String userId) {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        if (Strings.isEmpty(userId)) {
            return false;
        }
        if (Strings.isEquals(AppContext.me().getUserId(), userId)) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统版本
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            Log.e("version", e.toString());
        }
        return version;
    }


    /**
     * 初始化webView
     */
    public static void initWebView(WebView webView) {
        WebSettings s = webView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置缓存模式
        s.setAppCacheEnabled(false);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBuiltInZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }


    /**
     * 设置Edit光标位置
     */
    public static void setEditSelection(EditText editSelection) {
        Editable etext = editSelection.getText();
        Selection.setSelection(etext, etext.length());
    }


    /**
     * 获取链接
     */
    public static List<String> getUrl(Context context) {
        List<String> urls = new ArrayList<>();
        MetadataDTO dto = (MetadataDTO) MetadataUtil.getGCache(context, Constants.METADATA_DATA_KEY);
        if (dto != null) {
            urls.add(dto.getLiangce());
            urls.add(dto.getUpdateExplain());
            urls.add(dto.getDisclaimer());
            urls.add(dto.getPartner());
            urls.add(dto.getProtocol());
            urls.add(dto.getMediatorGuide());
            urls.add(dto.getMortgageGuide());
            urls.add(dto.getSimpleUserGuide());
            urls.add(dto.getRegisterGuide());
        }
        return urls;
    }


    /**
     * 判断是否跳转到锁屏界面
     */
    public static void isActionLockActivity(Context context) {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(context, MainActivity.class);
            return;
        }
        if (!Strings.isEmpty(Config.getString(AppContext.me().getPinKey()))) {
            IntentUtil.actionLockActivity(context, LockMode.VERIFY_PASSWORD, PinLockActivity.class, MainActivity.class);
        } else if (!Strings.isEmpty(Config.getString(AppContext.me().getGestureKey()))) {
            IntentUtil.actionLockActivity(context, LockMode.VERIFY_PASSWORD, GestureLockActivity.class, MainActivity.class);
        } else {
            IntentUtil.actionActivity(context, MainActivity.class);
        }
    }


    /**
     * 密码检查
     *
     * @param str
     * @return
     */
    public static boolean passwdCheck(String str) {
        String regEx = "[^\\u4E00-\\u9FA5]";
        String filter = stringFilter(regEx, str);
        if (filter.length() == 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 文本过滤
     *
     * @param regEx
     * @param text
     * @return
     */
    private static String stringFilter(String regEx, String text) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }


}
