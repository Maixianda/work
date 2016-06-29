package com.gzliangce.seccond_ver.util;

import com.gzliangce.AppContext;

import io.ganguo.library.util.Systems;
import io.ganguo.library.util.crypto.Rsas;

/**
 * Created by maixianda on 16-6-29.
 */
public class SaveParameter {
    private static SaveParameter ourInstance = new SaveParameter();

    public static SaveParameter getInstance() {
        return ourInstance;
    }

    private SaveParameter() {
    }

    public Tocken_signature_deviceId getTocken_signature_deviceId() {
        if (!AppContext.me().isLogined()) {
            return null;
        }
        String token = "";
        String signature = "";
        String timestamp = System.currentTimeMillis() + "";
        String deviceId = Systems.getDeviceId(AppContext.me());
        token = AppContext.me().getUser().getToken();
        signature = Rsas.getMD5(token + timestamp + deviceId);
        return new Tocken_signature_deviceId(token,signature,deviceId);
    }

    public class Tocken_signature_deviceId {
        private String mTocken = null;
        private String mSignature = null;
        private String mDeviceId = null;

        public Tocken_signature_deviceId(String mTocken, String mSignature, String mDeviceId) {
            this.mTocken = mTocken;
            this.mSignature = mSignature;
            this.mDeviceId = mDeviceId;
        }

        public String getmTocken() {
            return mTocken;
        }

        public String getmSignature() {
            return mSignature;
        }

        public String getmDeviceId() {
            return mDeviceId;
        }
    }
}
