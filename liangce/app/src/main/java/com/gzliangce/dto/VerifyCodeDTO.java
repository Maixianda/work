package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 16/1/11.
 * 获取验证码
 */
public class VerifyCodeDTO extends BaseDTO {
    /**
     * verifyCode : 7293
     */
    @SerializedName("verifyCode")
    private String verifyCode;

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    @Override
    public String toString() {
        return "VerifyCodeDTO{" +
                "verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
