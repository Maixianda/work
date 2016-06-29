package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 16/1/25.
 * 按揭员详情
 */
public class MortgageInfo implements Parcelable {

    /**
     * icon : http://xxx.jpg
     * phone : 13711111111
     * area : 白云区
     * accountId : 1
     * serviceCase : 3
     * introduce : 好
     * grade : 5
     * realName : mortgage
     * language : 国语,粤语
     * business : 公积金贷款，房屋贷款
     */

    private String icon;
    private String phone;
    private String area;
    @SerializedName("accountId")
    private int accountId;
    @SerializedName("serviceCase")
    private int serviceCase;
    @SerializedName("introduce")
    private String introduce;
    private float grade;
    @SerializedName("realName")
    private String realName;
    private String language;
    private String business;
    @SerializedName("isFavorite")
    private String isFavorite;

    protected MortgageInfo(Parcel in) {
        icon = in.readString();
        phone = in.readString();
        area = in.readString();
        accountId = in.readInt();
        serviceCase = in.readInt();
        introduce = in.readString();
        grade = in.readFloat();
        realName = in.readString();
        language = in.readString();
        business = in.readString();
        isFavorite = in.readString();
    }

    public static final Creator<MortgageInfo> CREATOR = new Creator<MortgageInfo>() {
        @Override
        public MortgageInfo createFromParcel(Parcel in) {
            return new MortgageInfo(in);
        }

        @Override
        public MortgageInfo[] newArray(int size) {
            return new MortgageInfo[size];
        }
    };

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getServiceCase() {
        return serviceCase;
    }

    public void setServiceCase(int serviceCase) {
        this.serviceCase = serviceCase;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(phone);
        dest.writeString(area);
        dest.writeInt(accountId);
        dest.writeInt(serviceCase);
        dest.writeString(introduce);
        dest.writeFloat(grade);
        dest.writeString(realName);
        dest.writeString(language);
        dest.writeString(business);
        dest.writeString(isFavorite);
    }

    @Override
    public String toString() {
        return "MortgageInfo{" +
                "icon='" + icon + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", accountId=" + accountId +
                ", serviceCase=" + serviceCase +
                ", introduce='" + introduce + '\'' +
                ", grade=" + grade +
                ", realName='" + realName + '\'' +
                ", language='" + language + '\'' +
                ", business='" + business + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                '}';
    }
}
