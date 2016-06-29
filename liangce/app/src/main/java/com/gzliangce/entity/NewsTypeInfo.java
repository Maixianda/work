package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/3/17.
 * 资讯/学院首页类型
 */
public class NewsTypeInfo implements Serializable {
    /**
     * typeId : 1
     * typeName : 企业资讯
     */

    @SerializedName("typeId")
    private int typeId;
    @SerializedName("typeName")
    private String typeName;
    @SerializedName("logo")
    private String logo;

    public NewsTypeInfo() {
    }

    public NewsTypeInfo(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public NewsTypeInfo(String typeName, String logo) {
        this.typeName = typeName;
        this.logo = logo;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "NewsTypeInfo{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
