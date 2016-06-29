package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tony on 10/6/15.
 * 登录用户信息
 */
public class AccountInfo {
    /**
     * accountId : 176
     * accountName : 小明
     * phone : 18613117395
     * status : normal
     * type : mediator
     * token : 591f1619c5be1874bc2ca7e54916a897
     * info : {"status":"noauth","otherCard":null,"identityCard":null}
     */

    @SerializedName("accountId")
    private int accountId;
    @SerializedName("realName")
    private String realName;
    private String phone;
    private String status;
    private String type;
    private String token;
    private String icon;
    /**
     * status : noauth
     * otherCard : null
     * identityCard : null
     */


    private AccountStatusInfo info;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountStatusInfo getInfo() {
        return info;
    }

    public void setInfo(AccountStatusInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "accountId=" + accountId +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", token='" + token + '\'' +
                ", icon='" + icon + '\'' +
                ", info=" + info +
                '}';
    }
}
