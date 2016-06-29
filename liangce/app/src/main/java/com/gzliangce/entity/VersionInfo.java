package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/5/31.
 * 版本信息详情
 */
public class VersionInfo implements Serializable, Parcelable {

    /**
     * version : 1.0.1
     * note : 修复闪退bug
     * downloadUrl : http://www.baidu.com/jinfu.apk
     */

    @SerializedName("version")
    private String version;
    @SerializedName("note")
    private String note;
    @SerializedName("downloadUrl")
    private String downloadUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.note);
        dest.writeString(this.downloadUrl);
    }

    public VersionInfo() {
    }

    protected VersionInfo(Parcel in) {
        this.version = in.readString();
        this.note = in.readString();
        this.downloadUrl = in.readString();
    }

    public static final Creator<VersionInfo> CREATOR = new Creator<VersionInfo>() {
        @Override
        public VersionInfo createFromParcel(Parcel source) {
            return new VersionInfo(source);
        }

        @Override
        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };

    @Override
    public String toString() {
        return "VersionInfo{" +
                "version='" + version + '\'' +
                ", note='" + note + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
