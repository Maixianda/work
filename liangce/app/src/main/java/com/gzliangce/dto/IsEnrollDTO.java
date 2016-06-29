package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 16/3/24.
 * 是否已经报名
 */
public class IsEnrollDTO extends BaseDTO {
    @SerializedName("isEnroll")
    private boolean isEnroll;

    public boolean isEnroll() {
        return isEnroll;
    }

    public void setEnroll(boolean enroll) {
        isEnroll = enroll;
    }

    @Override
    public String toString() {
        return "IsEnrollDTO{" +
                "isEnroll=" + isEnroll +
                '}';
    }
}
