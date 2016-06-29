package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/2/29.
 * 税率相关
 */
public class RateListInfo implements Serializable {
    @SerializedName("fundRate")//公积金利率
    private double fundRate;
    @SerializedName("businessRate")//商业贷款
    private double businessRate;
    @SerializedName("rateDate")//利率日期
    private long rateDate;
    @SerializedName("description")
    private String description;
    @SerializedName("newSimpleEntity")//新房-普通&经济
    private NewSimpleEntity newSimpleEntity;
    @SerializedName("newUnSimpleEntity")//新房-非普通
    private NewSimpleEntity newUnSimpleEntity;
    @SerializedName("oldSimpleEntity")//二手房-普通&经济
    private OldSimpleEntity oldSimpleEntity;
    @SerializedName("oldUnSimpleEntity")//二手房-非普通
    private OldSimpleEntity oldUnSimpleEntity;

    public double getFundRate() {
        return fundRate;
    }

    public void setFundRate(double fundRate) {
        this.fundRate = fundRate;
    }

    public double getBusinessRate() {
        return businessRate;
    }

    public void setBusinessRate(double businessRate) {
        this.businessRate = businessRate;
    }

    public long getRateDate() {
        return rateDate;
    }

    public void setRateDate(long rateDate) {
        this.rateDate = rateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NewSimpleEntity getNewSimpleEntity() {
        return newSimpleEntity;
    }

    public void setNewSimpleEntity(NewSimpleEntity newSimpleEntity) {
        this.newSimpleEntity = newSimpleEntity;
    }

    public NewSimpleEntity getNewUnSimpleEntity() {
        return newUnSimpleEntity;
    }

    public void setNewUnSimpleEntity(NewSimpleEntity newUnSimpleEntity) {
        this.newUnSimpleEntity = newUnSimpleEntity;
    }

    public OldSimpleEntity getOldSimpleEntity() {
        return oldSimpleEntity;
    }

    public void setOldSimpleEntity(OldSimpleEntity oldSimpleEntity) {
        this.oldSimpleEntity = oldSimpleEntity;
    }

    public OldSimpleEntity getOldUnSimpleEntity() {
        return oldUnSimpleEntity;
    }

    public void setOldUnSimpleEntity(OldSimpleEntity oldUnSimpleEntity) {
        this.oldUnSimpleEntity = oldUnSimpleEntity;
    }

    @Override
    public String toString() {
        return "RateListInfo{" +
                "fundRate=" + fundRate +
                ", businessRate=" + businessRate +
                ", rateDate=" + rateDate +
                ", description='" + description + '\'' +
                ", newSimpleEntity=" + newSimpleEntity +
                ", newUnSimpleEntity=" + newUnSimpleEntity +
                ", oldSimpleEntity=" + oldSimpleEntity +
                ", oldUnSimpleEntity=" + oldUnSimpleEntity +
                '}';
    }
}
