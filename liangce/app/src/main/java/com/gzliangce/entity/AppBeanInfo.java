package com.gzliangce.entity;


import com.pgyersdk.javabean.AppBean;

import java.io.Serializable;

/**
 * Created by leo on 16/5/27.
 * 序列化版本升级信息
 */
public class AppBeanInfo implements Serializable {
    private String versionName;
    private String downloadURL;
    private String versionCode;
    private String releaseNote;

    public static AppBeanInfo copyAppBean(AppBean appBean) {
        AppBeanInfo info = new AppBeanInfo();
        info.setDownloadURL(appBean.getDownloadURL());
        info.setReleaseNote(appBean.getReleaseNote());
        info.setVersionCode(appBean.getVersionCode());
        info.setVersionName(appBean.getVersionName());
        return info;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    @Override
    public String toString() {
        return "AppBeanInfo{" +
                "versionName='" + versionName + '\'' +
                ", downloadURL='" + downloadURL + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", releaseNote='" + releaseNote + '\'' +
                '}';
    }
}
