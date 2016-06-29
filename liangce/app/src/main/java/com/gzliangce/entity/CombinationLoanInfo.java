package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/23.
 * 组合贷款 - 数据实体类
 */
public class CombinationLoanInfo implements Serializable {
    private double bLoanSum;//商业贷款总额
    private int bMortgageMonth;//商业贷款按揭月数
    private double bInterestrate;//商业贷款利率


    private double aLoanSum;//公积金贷款总额
    private int aMortgageMonth;//公积金贷款按揭月数
    private double aInterestrate;//公积金利率

    public double getbLoanSum() {
        return bLoanSum;
    }

    public void setbLoanSum(double bLoanSum) {
        this.bLoanSum = bLoanSum;
    }

    public int getbMortgageMonth() {
        return bMortgageMonth;
    }

    public void setbMortgageMonth(int bMortgageMonth) {
        this.bMortgageMonth = bMortgageMonth;
    }

    public double getbInterestrate() {
        return bInterestrate;
    }

    public void setbInterestrate(double bInterestrate) {
        this.bInterestrate = bInterestrate;
    }

    public double getaLoanSum() {
        return aLoanSum;
    }

    public void setaLoanSum(double aLoanSum) {
        this.aLoanSum = aLoanSum;
    }

    public int getaMortgageMonth() {
        return aMortgageMonth;
    }

    public void setaMortgageMonth(int aMortgageMonth) {
        this.aMortgageMonth = aMortgageMonth;
    }

    public double getaInterestrate() {
        return aInterestrate;
    }

    public void setaInterestrate(double aInterestrate) {
        this.aInterestrate = aInterestrate;
    }

    @Override
    public String toString() {
        return "CombinationLoanInfo{" +
                "bLoanSum=" + bLoanSum +
                ", bMortgageMonth=" + bMortgageMonth +
                ", bInterestrate=" + bInterestrate +
                ", aLoanSum=" + aLoanSum +
                ", aMortgageMonth=" + aMortgageMonth +
                ", aInterestrate=" + aInterestrate +
                '}';
    }
}
