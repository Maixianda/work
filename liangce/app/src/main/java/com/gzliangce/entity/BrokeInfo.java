package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aaron on 1/20/16.
 * 金融经纪列表数据对象
 */
public class BrokeInfo implements Parcelable, Serializable {
    @SerializedName("icon")
    private String icon;
    @SerializedName("phone")
    private String phone;
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("grade")
    private float grade;
    @SerializedName("realName")
    private String realName;
    @SerializedName("language")
    private String language;
    @SerializedName("business")
    private String business;
    @SerializedName("introduce")
    private String introduce;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static Creator<BrokeInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "BrokeInfo{" +
                "icon='" + icon + '\'' +
                ", phone='" + phone + '\'' +
                ", accountId=" + accountId +
                ", grade=" + grade +
                ", realName='" + realName + '\'' +
                ", language='" + language + '\'' +
                ", business='" + business + '\'' +
                ", introduce='" + introduce + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeString(this.phone);
        dest.writeInt(this.accountId);
        dest.writeFloat(this.grade);
        dest.writeString(this.realName);
        dest.writeString(this.language);
        dest.writeString(this.business);
        dest.writeString(this.introduce);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
    }

    public BrokeInfo() {
    }

    protected BrokeInfo(Parcel in) {
        this.icon = in.readString();
        this.phone = in.readString();
        this.accountId = in.readInt();
        this.grade = in.readFloat();
        this.realName = in.readString();
        this.language = in.readString();
        this.business = in.readString();
        this.introduce = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }

    public static final Creator<BrokeInfo> CREATOR = new Creator<BrokeInfo>() {
        public BrokeInfo createFromParcel(Parcel source) {
            return new BrokeInfo(source);
        }

        public BrokeInfo[] newArray(int size) {
            return new BrokeInfo[size];
        }
    };
}
