package com.gzliangce;

import android.content.Context;
import android.util.Log;

import io.ganguo.library.Config;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.FileLogger;
import io.ganguo.library.util.log.LogConfig;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * App 环境配置
 * <p/>
 * Created by Tony on 3/4/15.
 */
public class AppEnv {

    private static Logger LOG = LoggerFactory.getLogger(AppEnv.class);

    /**
     * 开发环境配置信息
     * 根据app/build.gradle生成
     */
    public final static boolean isStage = BuildConfig.isStage;// 测试服务器true, 生产服务器为false
    public final static boolean isDebug = Strings.isEquals("debug", BuildConfig.BUILD_TYPE);// debug包为true, release包为false
    public final static String BASE_URL = BuildConfig.BASE_URL;//server url
    public final static String LEAN_CLOUD_ID = BuildConfig.LEAN_CLOUD_ID;//聊天 leancloud id
    public final static String LEAN_CLOUD_KEY = BuildConfig.LEAN_CLOUD_KEY;//聊天 leancloud key

    /**
     * init
     */
    public static void init(Context context) {
        // App 数据保存目录
        Config.DATA_PATH = BuildConfig.DATA_PATH;
        // 日志配置
        LogConfig.LOGGER_CLASS = FileLogger.class;
        LogConfig.PRIORITY = BuildConfig.LOG_LEVEL;
        LogConfig.FILE_PRIORITY = Log.ERROR;

        // app info
        LOG.i("****** AppEnvironment ******");
        LOG.i(" APPLICATION_ID: " + BuildConfig.APPLICATION_ID);
        LOG.i(" isStage: " + isStage);
        LOG.i(" FLAVOR: " + BuildConfig.FLAVOR);
        LOG.i(" BUILD_TYPE: " + BuildConfig.BUILD_TYPE);
        LOG.i(" isDebug: " + isDebug);
        LOG.i(" VERSION_CODE: " + BuildConfig.VERSION_CODE);
        LOG.i(" VERSION_NAME: " + BuildConfig.VERSION_NAME);
        LOG.i(" ScreenSize: " + Systems.getScreenWidth(context) + "x" + Systems.getScreenHeight(context));
        LOG.i(" Device Performance: " + Systems.getPerformance(context));
        LOG.i(" BASE_URL: " + BASE_URL);
        LOG.i(" DATA_PATH: " + Config.DATA_PATH);
        LOG.i(" LOG_CONSOLE: " + LogConfig.PRIORITY);
        LOG.i(" LOG_FILE: " + LogConfig.FILE_PRIORITY);
        LOG.i("***************************");
    }

}
