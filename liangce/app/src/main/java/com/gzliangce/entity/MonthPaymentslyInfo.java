package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/17.
 * 详细月供数据对象
 */
public class MonthPaymentslyInfo implements Serializable {
    private double month;//月份
    private double payments;//月供
    private double principal;//本金
    private double interest;//利息
    private double remainingLoan;//剩余贷款

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getPayments() {
        return payments;
    }

    public void setPayments(double payments) {
        this.payments = payments;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getRemainingLoan() {
        return remainingLoan;
    }

    public void setRemainingLoan(double remainingLoan) {
        this.remainingLoan = remainingLoan;
    }

    @Override
    public String toString() {
        return "MonthPaymentslyInfo{" +
                "month=" + month +
                ", payments=" + payments +
                ", principal=" + principal +
                ", interest=" + interest +
                ", remainingLoan=" + remainingLoan +
                '}';
    }
}
