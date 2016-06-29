package com.gzliangce.util;

import android.text.TextUtils;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.dialog.ErrorPasswordDialog;

import io.ganguo.library.AppManager;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by aaron on 29/2/16.
 */
public class AVImClientManagerUtil {
    private static Logger logger = LoggerFactory.getLogger(AVImClientManagerUtil.class);
    private static AVImClientManagerUtil imClientManager;
    private AVIMClient client;
    private String clientId;

    public synchronized static AVImClientManagerUtil getInstance() {
        if (null == imClientManager) {
            imClientManager = new AVImClientManagerUtil();
        }
        return imClientManager;
    }

    private AVImClientManagerUtil() {
    }

    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        client = AVIMClient.getInstance(clientId, clientId);
        client.open(callback);
        client.setClientEventHandler(new AVIMClientEventHandler() {
            @Override
            public void onConnectionPaused(AVIMClient avimClient) {

            }

            @Override
            public void onConnectionResume(AVIMClient avimClient) {

            }

            @Override
            public void onClientOffline(AVIMClient avimClient, int i) {
                if (i == 4111) {
                    doColseClient();
                    DialogUtil.showLogoutDialog();
                }
            }
        });
    }

    public AVIMClient getClient() {
        return client;
    }

    public void doColseClient() {
        this.client = null;
    }

    public String getClientId() {
        if (TextUtils.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AVImClientManager.open first");
        }
        return clientId;
    }

    public void close(AVIMClientCallback callback) {
        client.close(callback);
    }


}
