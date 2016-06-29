package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/26.
 * 二手房 - 买方缴纳
 */
public class OldHouseSellTaxInfo implements Serializable {
    private double stampTax;//印花税
    private double poundageFee;//手续费
    private double businessTax;//营业税
    private double individualIncomeTax;//个人所得税
    private double LandTax;//综合地价税

    public double getStampTax() {
        return stampTax;
    }

    public void setStampTax(double stampTax) {
        this.stampTax = stampTax;
    }

    public double getPoundageFee() {
        return poundageFee;
    }

    public void setPoundageFee(double poundageFee) {
        this.poundageFee = poundageFee;
    }

    public double getBusinessTax() {
        return businessTax;
    }

    public void setBusinessTax(double businessTax) {
        this.businessTax = businessTax;
    }

    public double getIndividualIncomeTax() {
        return individualIncomeTax;
    }

    public void setIndividualIncomeTax(double individualIncomeTax) {
        this.individualIncomeTax = individualIncomeTax;
    }

    public double getLandTax() {
        return LandTax;
    }

    public void setLandTax(double landTax) {
        LandTax = landTax;
    }

    @Override
    public String toString() {
        return "OldHouseSellTaxInfo{" +
                "stampTax=" + stampTax +
                ", poundageFee=" + poundageFee +
                ", businessTax=" + businessTax +
                ", individualIncomeTax=" + individualIncomeTax +
                ", LandTax=" + LandTax +
                '}';
    }
}
