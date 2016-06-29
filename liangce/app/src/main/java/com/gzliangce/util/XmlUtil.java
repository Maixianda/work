package com.gzliangce.util;

import android.view.View;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.UserState;
import com.gzliangce.enums.AttestationType;
import com.gzliangce.enums.OrderStatusType;
import com.gzliangce.enums.UserType;
import com.mob.tools.utils.Data;

import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/6.
 * 工具类
 */
public class XmlUtil {
    private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    public static String orderStatusShow(String orderStatus) {
        if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.wait))) {
            return "等待中";
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.receive))) {
            return "已接单";
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.cancel))) {
            return "已取消";
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.signed))) {
            return "已签约";
        }
        return "";
    }

    public static int orderStatusColor(String orderStatus) {
        if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.wait))) {
            return 0xfffa4141;
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.receive))) {
            return 0xff3399ff;
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.cancel))) {
            return 0xfffa4141;
        } else if (Strings.isEquals(orderStatus, String.valueOf(OrderStatusType.signed))) {
            return 0xff3399ff;
        }
        return 0xfffa4141;
    }

    /**
     * 指定用户类型显示
     *
     * @param type
     * @param typeValues
     * @return
     */
    public static int displayShow(String type, UserType typeValues) {
        if (Strings.isNotEmpty(type)) {
            if (Strings.isEquals(type, String.valueOf(typeValues)))
                return View.VISIBLE;
        }
        return View.GONE;
    }


    public static boolean isMortgageAndOrderStuts(OrderInfo orderInfo) {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        if (LiangCeUtil.judgeUserType(UserType.mortgage) && Strings.isEquals(orderInfo.getStatus(), OrderStatusType.signed.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 指定用户类型隐藏
     *
     * @param type
     * @param typeValues
     * @return
     */
    public static int displayHide(String type, UserType typeValues) {
        if (Strings.isNotEmpty(type)) {
            if (Strings.isEquals(type, String.valueOf(typeValues)))
                return View.GONE;
        }
        return View.VISIBLE;
    }

    /**
     * 判断用户是否是按揭
     *
     * @return
     */
    public static String judgeUserIsMediator(OrderInfo orderInfo) {
        if (!AppContext.me().isLogined() || orderInfo == null) {
            return "金融经纪：";
        }
        if (LiangCeUtil.getUserType() == UserType.mortgage) {
            if (Strings.isEquals(orderInfo.getStatus(), OrderStatusType.signed.toString())) {
                return "借款人姓名：";
            }
            return "中介：";
        }
        return "金融经纪：";
    }

    /**
     * 获取字段姓名 买家or中介
     */
    public static String getRealName(OrderInfo orderInfo) {
        if (!AppContext.me().isLogined() || orderInfo == null) {
            return "";
        }
        if (LiangCeUtil.getUserType() != UserType.mortgage) {
            return orderInfo.getRealName();
        }
        if (Strings.isEquals(orderInfo.getStatus(), OrderStatusType.signed.toString())) {
            return orderInfo.getBuyer();
        } else {
            return orderInfo.getRealName();
        }

    }

    /**
     * im强制转换对象
     */
    public static String getMessageContent(AVIMMessage avimMessage) {
        if (avimMessage == null) {
            return "";
        }
        return avimMessage.getContent();
    }

    /**
     * 发送信息显示loading。。。
     */
    public static boolean showSendStatus(AVIMMessage avimMessage) {
        if (avimMessage == null) {
            return false;
        }
        if (avimMessage.getMessageStatus().getStatusCode() == AVIMMessage.AVIMMessageStatus.AVIMMessageStatusSending.getStatusCode()) {
            return true;
        }
        if (avimMessage.getMessageStatus().getStatusCode() == AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed.getStatusCode()) {
            return true;
        }
        return false;
    }

    /**
     * 发送信息显示loading。。。
     */
    public static int changeSendIcon(AVIMMessage avimMessage) {
        if (avimMessage == null) {
            return R.drawable.frame_loading;
        }
        if (avimMessage.getMessageStatus().getStatusCode() == AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed.getStatusCode()) {
            return R.drawable.ic_send_failed;
        }
        return R.drawable.frame_loading;
    }

    public static boolean showUnReadCount(int unReadCount) {
        if (unReadCount == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String displayUnReadCount(int unReadCount) {
        if (unReadCount == 0) {
            return "";
        } else {
            return unReadCount + "";
        }
    }

    public static String showTransforDate(Data data) {
        return data.toString();
    }

    public static String displayContactsTitle() {
        if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            return "联系金融经纪";
        } else if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            return "联系中介";
        } else {
            return "联系金融经纪";
        }
    }

    public static boolean displayVedioIcon(String from) {
        if (Strings.isEquals(from, "video")) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户认证状态
     *
     * @return
     */
    public static boolean isAutherizeStatus(UserState state, AttestationType type) {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        if (state == null) {
            return false;
        }
        if (!LiangCeUtil.judgeUserType(UserType.mediator)) {
            return false;
        }
        if (Strings.isEquals(state.getAttestState().get(), type.toString())) {
            return true;
        }
        return false;
    }


    /**
     * 获取已经认证状态图标
     *
     * @return
     */
    public static boolean isPassVisbile() {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        if (!LiangCeUtil.judgeUserType(UserType.mediator)) {
            return false;
        }
        if (Strings.isEquals(AppContext.me().getUser().getInfo().getStatus(), AttestationType.pass.toString())) {
            return true;
        }
        return false;
    }



}
