package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/22.
 * 利率
 */
public class InterestrateInfo implements Serializable {
    private String interestrateDate;//利率日期
    private double interestrate;//利率
    private double interestrateDiscount;//利率折扣
    private String insterestName;//利率名称
    private int interestrateIndex;//利率折扣所在位置，例如基准利率，位置为0
    private long rateDate;//利率日期

    public long getRateDate() {
        return rateDate;
    }

    public void setRateDate(long rateDate) {
        this.rateDate = rateDate;
    }

    public InterestrateInfo() {
    }

    public InterestrateInfo(String interestrateDate, double interestrate) {
        this.interestrateDate = interestrateDate;
        this.interestrate = interestrate;
    }

    public String getInterestrateDate() {
        return interestrateDate;
    }

    public void setInterestrateDate(String interestrateDate) {
        this.interestrateDate = interestrateDate;
    }

    public double getInterestrate() {
        return interestrate;
    }

    public void setInterestrate(double interestrate) {
        this.interestrate = interestrate;
    }

    public double getInterestrateDiscount() {
        return interestrateDiscount;
    }

    public void setInterestrateDiscount(double interestrateDiscount) {
        this.interestrateDiscount = interestrateDiscount;
    }

    public String getInsterestName() {
        return insterestName;
    }

    public void setInsterestName(String insterestName) {
        this.insterestName = insterestName;
    }

    public int getInterestrateIndex() {
        return interestrateIndex;
    }

    public void setInterestrateIndex(int interestrateIndex) {
        this.interestrateIndex = interestrateIndex;
    }

    @Override
    public String toString() {
        return "InterestrateInfo{" +
                "interestrateDate='" + interestrateDate + '\'' +
                ", interestrate=" + interestrate +
                ", interestrateDiscount=" + interestrateDiscount +
                ", insterestName='" + insterestName + '\'' +
                ", interestrateIndex=" + interestrateIndex +
                ", rateDate=" + rateDate +
                '}';
    }
}
