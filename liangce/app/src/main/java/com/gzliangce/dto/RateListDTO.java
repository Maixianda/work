package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.RateListInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/2/29.
 * 利率数据对象
 */
public class RateListDTO extends BaseDTO implements Serializable {
    @SerializedName("landValueIncrementTax")
    private String landValueIncrementTax;
    @SerializedName("secondHandHouseTax")
    private String secondHandHouseTax;
    @SerializedName("newHouseTax")
    private String newHouseTax;
    @SerializedName("rateList")
    private List<RateListInfo> rateList;

    public String getLandValueIncrementTax() {
        return landValueIncrementTax;
    }

    public void setLandValueIncrementTax(String landValueIncrementTax) {
        this.landValueIncrementTax = landValueIncrementTax;
    }

    public String getSecondHandHouseTax() {
        return secondHandHouseTax;
    }

    public void setSecondHandHouseTax(String secondHandHouseTax) {
        this.secondHandHouseTax = secondHandHouseTax;
    }

    public String getNewHouseTax() {
        return newHouseTax;
    }

    public void setNewHouseTax(String newHouseTax) {
        this.newHouseTax = newHouseTax;
    }

    public List<RateListInfo> getRateList() {
        return rateList;
    }

    public void setRateList(List<RateListInfo> rateList) {
        this.rateList = rateList;
    }

    @Override
    public String toString() {
        return "RateListDTO{" +
                "landValueIncrementTax='" + landValueIncrementTax + '\'' +
                ", secondHandHouseTax='" + secondHandHouseTax + '\'' +
                ", newHouseTax='" + newHouseTax + '\'' +
                ", rateList=" + rateList +
                '}';
    }
}
