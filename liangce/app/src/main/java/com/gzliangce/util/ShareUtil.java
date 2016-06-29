package com.gzliangce.util;

import android.content.Context;

import com.google.repacked.apache.commons.io.FileUtils;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.ganguo.library.util.Files;
import io.ganguo.library.util.Strings;

/**
 * Created by leo on 16/1/30.
 * 分享工具类
 */
public class ShareUtil {
    /**
     * 分享复用方法
     */
    public static void shareApp(Context context, String platform, String title, String content, String url, PlatformActionListener callback) {
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setText(content);
        oks.setUrl(url);
        oks.setTitleUrl(url);
        oks.setImagePath(Files.getAssetsToPath(context, "ic_launcher.png"));
        oks.setSilent(false);
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setCallback(callback);
        oks.show(context);
    }

    /**
     * 分享到微信朋友圈
     */
    public static void momentShare(Context context, String content, String url) {
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(content);
        oks.setUrl(url);
        oks.setImagePath(Files.getAssetsToPath(context, "ic_launcher.png"));
        oks.setSilent(false);
        if (WechatMoments.NAME != null) {
            oks.setPlatform(WechatMoments.NAME);
        }
        oks.show(context);
    }

}
