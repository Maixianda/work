package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/24.
 * 新房税费
 */
public class NewHouseTaxFeeInfo implements Serializable {
    private double fairFee;//公正费
    private double deedTax;//契税
    private double stampTax;//印花税
    private double propertyFee;//产权费
    private double poundageFee;//手续费
    private double totalFee;//总费用
    private double trendtampTax;//工本印花税

    public double getTrendtampTax() {
        return trendtampTax;
    }

    public void setTrendtampTax(double trendtampTax) {
        this.trendtampTax = trendtampTax;
    }

    public double getFairFee() {
        return fairFee;
    }

    public void setFairFee(double fairFee) {
        this.fairFee = fairFee;
    }

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

    public double getPropertyFee() {
        return propertyFee;
    }

    public void setPropertyFee(double propertyFee) {
        this.propertyFee = propertyFee;
    }

    public double getPoundageFee() {
        return poundageFee;
    }

    public void setPoundageFee(double poundageFee) {
        this.poundageFee = poundageFee;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    @Override
    public String toString() {
        return "NewHouseTaxFeeInfo{" +
                "fairFee=" + fairFee +
                ", deedTax=" + deedTax +
                ", stampTax=" + stampTax +
                ", propertyFee=" + propertyFee +
                ", poundageFee=" + poundageFee +
                ", totalFee=" + totalFee +
                ", trendtampTax=" + trendtampTax +
                '}';
    }
}
