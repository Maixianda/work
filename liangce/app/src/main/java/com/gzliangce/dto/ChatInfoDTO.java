package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aaron on 2/27/16.
 */
public class ChatInfoDTO extends BaseDTO {
    @SerializedName("icon")
    private String icon;
    @SerializedName("realName")
    private String realName;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "ChatInfoDTO{" +
                "icon='" + icon + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }
}
