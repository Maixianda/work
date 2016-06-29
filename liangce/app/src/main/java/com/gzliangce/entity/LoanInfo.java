package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/18.
 * 普通贷款 - 数据对象
 */
public class LoanInfo implements Serializable {
    private int houseSum;//房产总额
    private double mortgageRate;//分期比例
    private double loanSum;//贷款总额
    private int mortgageMonth;//分期月数
    private double interestrate;//贷款利率
    private double loanPerMonth;//月均还款

    public double getLoanPerMonth() {
        return loanPerMonth;
    }

    public void setLoanPerMonth(double loanPerMonth) {
        this.loanPerMonth = loanPerMonth;
    }

    public double getHouseSum() {
        return houseSum;
    }

    public void setHouseSum(int houseSum) {
        this.houseSum = houseSum;
    }

    public double getMortgageRate() {
        return mortgageRate;
    }

    public void setMortgageRate(double mortgageRate) {
        this.mortgageRate = mortgageRate;
    }

    public double getLoanSum() {
        return loanSum;
    }

    public void setLoanSum(double loanSum) {
        this.loanSum = loanSum;
    }

    public int getMortgageMonth() {
        return mortgageMonth;
    }

    public void setMortgageMonth(int mortgageMonth) {
        this.mortgageMonth = mortgageMonth;
    }

    public double getInterestrate() {
        return interestrate;
    }

    public void setInterestrate(double interestrate) {
        this.interestrate = interestrate;
    }

    @Override
    public String toString() {
        return "LoanInfo{" +
                "houseSum=" + houseSum +
                ", mortgageRate=" + mortgageRate +
                ", loanSum=" + loanSum +
                ", mortgageMonth=" + mortgageMonth +
                ", interestrate=" + interestrate +
                ", loanPerMonth=" + loanPerMonth +
                '}';
    }
}
