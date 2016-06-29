package com.gzliangce;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.gzliangce.bean.Constants;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.enums.AttestationType;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.chat.IMConversationHandler;
import com.gzliangce.ui.activity.chat.MessageHandler;
import com.gzliangce.util.UpdateManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.squareup.otto.Subscribe;

import io.ganguo.library.AppManager;
import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.OnExitEvent;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.gson.Gsons;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import io.ganguo.opensdk.OpenSDK;

/**
 * App 上下文环境
 * <p>
 * Created by Tony on 9/30/15.
 */
public class AppContext extends BaseApp {
    private final static Logger logger = LoggerFactory.getLogger(AppContext.class);
    /**
     * 登录用户
     */
    private AccountInfo accountInfo = null;
    public static boolean isBackGround = false;
    public static AVInstallation avInstallation;

    /**
     * 是否弹升级提示弹窗
     */
    private boolean isUpDate = true;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化环境变量
        AppEnv.init(this);
        // init libs
        OpenSDK.init(this, AppEnv.isStage);
        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        AVOSCloud.initialize(this, AppEnv.LEAN_CLOUD_ID, AppEnv.LEAN_CLOUD_KEY);

        // 开启消息未读消息
        AVIMClient.setOfflineMessagePush(true);

        // 注册接受未读消息
        AVIMMessageManager.setConversationEventHandler(new IMConversationHandler());

        // 应用一启动就会重连，服务器会推送离线消息过来，需要 MessageHandler 来处理
        AVIMMessageManager.registerDefaultMessageHandler(new MessageHandler(this));

        // 设置默认打开的 Activity
        PushService.setDefaultPushCallback(this, MainActivity.class);
        if (isLogined()) {
            setAvInstallation(getUserId());
        }
    }

    /**
     * 应用退出事件
     *
     * @param event
     */
    @Subscribe
    public void onExitEvent(OnExitEvent event) {
        logger.d("on app exit, event:" + event);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 获取上一文环境实例
     *
     * @return
     */
    public static AppContext me() {
        return BaseApp.me();
    }

    /**
     * 获取登录用户，如果没有登录返回 null
     *
     * @return
     */
    public AccountInfo getUser() {
        if (accountInfo == null) {
            String userStr = Config.getString(Constants.CONFIG_LOGIN_USER);
            if (Strings.isNotEmpty(userStr)) {
                accountInfo = Gsons.fromJson(userStr, AccountInfo.class);
            }
        }
        return accountInfo;
    }

    /**
     * 登录成功后设置用户，持久保存到本地
     *
     * @param info
     */
    public void setUser(AccountInfo info) {
        accountInfo = info;
        if (info != null) {
            Config.putString(Constants.CONFIG_LOGIN_USER, Gsons.toJson(info));
        } else {
            Config.remove(Constants.CONFIG_LOGIN_USER);
        }
    }

    /**
     * 是否已经登录
     * user != null && user.getToken() != null
     *
     * @return
     */
    public boolean isLogined() {
        return getUser() != null;
    }


    /**
     * 获取用户Id
     */
    public String getUserId() {
        if (!AppContext.me().isLogined()) {
            return "";
        }
        return String.valueOf(AppContext.me().getUser().getAccountId());
    }


    /**
     * 中介用户是否通过认证
     */
    public boolean isAuthorization() {
        if (!AppContext.me().isLogined()) {
            return false;
        }
        if (getUser().getInfo() == null) {
            return false;
        }
        if (Strings.isEquals(getUser().getInfo().getStatus(), AttestationType.pass.toString())) {
            return true;
        }
        return false;
    }


    /**
     * 手势密码缓存key
     */
    public String getGestureKey() {
        return Constants.GESTURE_LOCK_PASSWORD_KEY + getUserId();
    }


    /**
     * PIN码缓存key
     */
    public String getPinKey() {
        return Constants.PIN_PASSWORD_KEY + AppContext.me().getUserId();
    }

    /**
     * 获取avInstallation
     */
    public AVInstallation getAvInstallation() {
        if (avInstallation == null) {
            avInstallation = AVInstallation.getCurrentInstallation();
        }
        return avInstallation;
    }

    /**
     * 设置avInstallation的ClientId
     */
    public void setAvInstallation(String userId) {
        if (avInstallation == null) {
            avInstallation = AVInstallation.getCurrentInstallation();
        }
        avInstallation.put(Constants.CLIENT_ID, userId);
        avInstallation.saveInBackground();
    }

    /**
     * 移除avInstallation的ClientId
     */
    public void removeAvInstallation() {
        if (avInstallation == null) {
            avInstallation = AVInstallation.getCurrentInstallation();
        }
        avInstallation.remove(Constants.CLIENT_ID);
        avInstallation.saveInBackground();
    }

    /**
     * 注册升级提示监听
     */
    public void registerUpDateApp() {
        if (!isUpDate) {
            return;
        }
        // 升级提示
        PgyUpdateManager.register(AppManager.getActivity(MainActivity.class), UpdateManager.getInstance().getUpdateListener(AppManager.getActivity(MainActivity.class)));
        UpdateManager.getInstance().isShowUpdateHint(AppContext.this);
        isUpDate = false;
    }

}
