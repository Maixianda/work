package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/26.
 * 二手房 - 买方缴纳税收
 */
public class OldHouseBuyTaxInfo implements Serializable {
    private double deedTax;//契税
    private double stampTax;//印花税
    private double trendtampTax;//工本印花税
    private double poundageFee;//手续费
    private double registration;//登记费
    private double totalFee;//总费用

    public double getDeedTax() {
        return deedTax;
    }

    public void setDeedTax(double deedTax) {
        this.deedTax = deedTax;
    }

    public double getStampTax() {
        return stampTax;
    }

    public void setStampTax(double stampTax) {
        this.stampTax = stampTax;
    }

    public double getTrendtampTax() {
        return trendtampTax;
    }

    public void setTrendtampTax(double trendtampTax) {
        this.trendtampTax = trendtampTax;
    }

    public double getPoundageFee() {
        return poundageFee;
    }

    public void setPoundageFee(double poundageFee) {
        this.poundageFee = poundageFee;
    }

    public double getRegistration() {
        return registration;
    }

    public void setRegistration(double registration) {
        this.registration = registration;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }


    @Override
    public String toString() {
        return "OldHouseBuyTaxInfo{" +
                "deedTax=" + deedTax +
                ", stampTax=" + stampTax +
                ", trendtampTax=" + trendtampTax +
                ", poundageFee=" + poundageFee +
                ", registration=" + registration +
                ", totalFee=" + totalFee +
                '}';
    }
}
