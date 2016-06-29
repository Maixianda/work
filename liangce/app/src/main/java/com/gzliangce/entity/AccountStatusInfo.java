package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import io.ganguo.library.util.Strings;

/**
 * Created by leo on 16/1/16.
 * 用户详细信息
 */
public class AccountStatusInfo {

    /**
     * status : noauth
     * otherCard : null
     * identityCard : null
     */

    private String status;
    @SerializedName("otherCard")
    private String otherCard;
    @SerializedName("identityCard")
    private String identityCard;
    private String reason;
    private String language;
    @SerializedName("serviceCase")
    private String serviceCase;
    private String business;
    private float grade;
    private String introduce;
    @SerializedName("organizationName")
    private String organizationName;
    private String branch;
    @SerializedName("isStaff")
    private boolean isStaff = false;


    //自定义字段
    private String functions;
    private boolean isLanguagePage;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public boolean isLanguagePage() {
        return isLanguagePage;
    }

    public void setIsLanguagePage(boolean isLanguagePage) {
        this.isLanguagePage = isLanguagePage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtherCard() {
        return otherCard;
    }

    public void setOtherCard(String otherCard) {
        this.otherCard = otherCard;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getServiceCase() {
        return serviceCase;
    }

    public void setServiceCase(String serviceCase) {
        this.serviceCase = serviceCase;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public void setLanguagePage(boolean languagePage) {
        isLanguagePage = languagePage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    @Override
    public String toString() {
        return "AccountStatusInfo{" +
                "status='" + status + '\'' +
                ", otherCard='" + otherCard + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", reason='" + reason + '\'' +
                ", language='" + language + '\'' +
                ", serviceCase='" + serviceCase + '\'' +
                ", business='" + business + '\'' +
                ", grade=" + grade +
                ", introduce='" + introduce + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", branch='" + branch + '\'' +
                ", isStaff=" + isStaff +
                ", functions='" + functions + '\'' +
                ", isLanguagePage=" + isLanguagePage +
                '}';
    }
}
